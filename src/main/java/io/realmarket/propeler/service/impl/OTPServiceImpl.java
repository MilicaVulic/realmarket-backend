package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.OTPWildcard;
import io.realmarket.propeler.model.enums.EAuthorizationActionType;
import io.realmarket.propeler.repository.AuthorizedActionRepository;
import io.realmarket.propeler.repository.OTPWildcardRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static io.realmarket.propeler.model.enums.EAuthorizationActionType.NEW_TOTP_SECRET;

@Service
@Slf4j
public class OTPServiceImpl implements OTPService {

  private AuthService authService; // needed for change secret
  private AuthorizedActionRepository authorizedActionRepository;
  private OTPWildcardRepository otpWildcardRepository;
  private PasswordEncoder passwordEncoder;

  @Value("${app.otp.secret.size}")
  private Integer otpSecretSize;

  @Value("${app.otp.wildcard.size}")
  private Integer otpWildcardSize;

  @Value("${app.otp.wildcard.batch_size}")
  private Integer otpWildcardBatchSize;

  @Autowired
  OTPServiceImpl(
      AuthService authService,
      AuthorizedActionRepository authorizedActionRepository,
      OTPWildcardRepository otpWildcardRepository,
      PasswordEncoder passwordEncoder) {
    this.authService = authService;
    this.otpWildcardRepository = otpWildcardRepository;
    this.authorizedActionRepository = authorizedActionRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public String generateTOTPSecret(Auth auth) {
    String secret = RandomStringBuilder.generateBase32String(otpSecretSize);
    String encriptedSecret = encryptSecret(secret);

    storeAuthorizationAction(auth, NEW_TOTP_SECRET, encriptedSecret, 3600 * 1000L);
    return secret;
  }

  @Transactional
  public List<String> generateRecoveryCodes(Auth auth) {
    LinkedList<String> recoveryCodes = new LinkedList<>();
    otpWildcardRepository.deleteAllByAuth(auth);
    for (int i = 0; i < otpWildcardBatchSize; i++) {
      String recoveryCode = RandomStringBuilder.generateBase32String(otpWildcardSize);
      recoveryCodes.add(recoveryCode);
      otpWildcardRepository.save(
          OTPWildcard.builder().auth(auth).wildcard(passwordEncoder.encode(recoveryCode)).build());
    }
    return recoveryCodes;
  }

  @Transactional
  public Boolean validate(Auth auth, TwoFADto code) {
    if ( StringUtils.hasText(code.getCode())) {
      return validateCode(auth.getTotpSecret(), code.getCode());
    } else if (StringUtils.hasText(code.getWildcard())) {
      return validateRecoveryCode(auth, code.getWildcard());
    }
    return false;
  }

  @Transactional
  public Boolean validateTOTPSecretChange(Auth auth, String totpcode) {
    AuthorizedAction authorizedAction = findAuthorizedActionOrThrow(auth, NEW_TOTP_SECRET);
    String newSecret = decryptSecret(authorizedAction.getData());
    if (!validateCode(newSecret, totpcode)) {
      return false;
    }
    // save new secret
    authService.updateSecretById(auth.getId(), newSecret);
    return true;
  }

  private void deleteByAuthAndType(Auth auth, EAuthorizationActionType type) {
    authorizedActionRepository.deleteAllByAuthAndType(auth, type);
    authorizedActionRepository.flush();
  }

  @Transactional
  public void storeAuthorizationAction(
      Auth auth, EAuthorizationActionType type, String data, Long mmTimeout) {
    deleteByAuthAndType(auth, type);
    log.info("Store authorization action.");
    AuthorizedAction authorizedAction =
        AuthorizedAction.builder()
            .auth(auth)
            .type(type)
            .data(data)
            .expiration(Instant.now().plusMillis(mmTimeout))
            .build();
    authorizedActionRepository.save(authorizedAction);
  }

  @Transactional
  public Optional<String> validateAuthorizationAction(
      Auth auth, EAuthorizationActionType type, String code) {
    if (type.equals(NEW_TOTP_SECRET)) {
      return Optional.empty();
    }
    AuthorizedAction authorizedAction = findAuthorizedActionOrThrow(auth, type);

    if (validate(auth, new TwoFADto(code, null))) {
      return Optional.of(authorizedAction.getData());
    }
    return Optional.empty();
  }

  private boolean validateCode(String encriptedSecret, String code) {
    Totp totp = new Totp(decryptSecret(encriptedSecret));
    return totp.verify(code);
  }

  private boolean validateRecoveryCode(Auth auth, String code) {
    return validateRecoveryCode(auth.getId(), code);
  }

  private boolean validateRecoveryCode(Long authId, String code) {
    List<OTPWildcard> otpWildcards = otpWildcardRepository.findAllByAuth(new Auth(authId));
    for (OTPWildcard otpWildcard : otpWildcards) {
      if (passwordEncoder.matches(code, otpWildcard.getWildcard())) {
        otpWildcardRepository.deleteById(otpWildcard.getId());
        return true;
      }
    }
    return false;
  }

  private AuthorizedAction findAuthorizedActionOrThrow(Auth auth, EAuthorizationActionType type) {
    Optional<AuthorizedAction> authorizedAction =
        authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(auth, type, Instant.now());
    if (!authorizedAction.isPresent()) {
      throw new EntityNotFoundException(ExceptionMessages.AUTHORIZATION_ACTION_NOT_FOUND);
    }
    return authorizedAction.get();
  }

  // Decript/Encript secret Take salt from property and use it as key for encription
  private String decryptSecret(String encriptedSecret) {
    return encriptedSecret;
  }

  private String encryptSecret(String secret) {
    return secret;
  }
}