package io.realmarket.propeler.util;

import io.realmarket.propeler.service.blockchain.dto.user.UserEmailChangeDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserPasswordChangeDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserRegenerationOfRecoveryDto;
import io.realmarket.propeler.service.blockchain.dto.user.UserRegistrationDto;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static io.realmarket.propeler.util.AuthUtils.TEST_EMAIL;

public class BlockchainUtils {

  public static final String TEST_IP = "127.0.0.1";
  public static final Long TEST_ID = 123L;
  public static final String TEST_USERNAME = "TEST_USERNAME";
  public static final String TEST_ROLE = "Role";
  public static final String TEST_MESSAGE = "message";

  public static final UserRegistrationDto TEST_REGISTRATION_DTO =
      UserRegistrationDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .role(TEST_ROLE)
          .username(TEST_USERNAME)
          .build();

  public static final UserPasswordChangeDto TEST_PASSWORD_CHANGE_DTO =
      UserPasswordChangeDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .build();

  public static final UserEmailChangeDto TEST_EMAIL_CHANGE_DTO =
      UserEmailChangeDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .newEmailHash(TEST_EMAIL)
          .build();

  public static final UserRegenerationOfRecoveryDto TEST_REGENERATION_OF_RECOVERY =
      UserRegenerationOfRecoveryDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .build();

  public static final Map<String, Object> getMapMocked(Boolean success) {
    Map<String, Object> m = new HashMap<>();
    m.put("success", success);
    m.put("message", TEST_MESSAGE);
    return m;
  }

  public static final ResponseEntity<Map> TEST_RESPONSE_OK = ResponseEntity.ok(getMapMocked(true));
  public static final ResponseEntity<Map> TEST_RESPONSE_ERROR =
      ResponseEntity.ok(getMapMocked(false));
}
