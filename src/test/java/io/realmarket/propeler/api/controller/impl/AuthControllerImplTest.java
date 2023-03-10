package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.util.AuthUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static io.realmarket.propeler.util.AuthUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class AuthControllerImplTest {
  @Mock private AuthService authService;

  @InjectMocks private AuthControllerImpl authControllerImpl;

  @Test
  public void RegisterEntrepreneur_Should_ReturnCreated() {
    ResponseEntity responseEntity =
        authControllerImpl.registerEntrepreneur(TEST_ENTREPRENEUR_REGISTRATION_DTO);

    verify(authService, Mockito.times(1)).registerEntrepreneur(TEST_ENTREPRENEUR_REGISTRATION_DTO);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test
  public void RegisterIndividualInvestor_Should_ReturnCreated() {
    ResponseEntity responseEntity =
        authControllerImpl.registerIndividualInvestor(TEST_REGISTRATION_DTO);

    verify(authService, Mockito.times(1))
        .register(TEST_REGISTRATION_DTO, TEST_ROLE_INDIVIDUAL_INVESTOR);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test
  public void RegisterCorporateInvestor_Should_ReturnCreated() {
    ResponseEntity responseEntity =
        authControllerImpl.registerCorporateInvestor(TEST_CORPORATE_INVESTOR_REGISTRATION_DTO);

    verify(authService, Mockito.times(1))
        .register(TEST_CORPORATE_INVESTOR_REGISTRATION_DTO, TEST_ROLE_CORPORATE_INVESTOR);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test
  public void ConfirmRegistration_Should_ReturnOK() {
    ResponseEntity responseEntity =
        authControllerImpl.confirmRegistration(TEST_CONFIRM_REGISTRATION_DTO);

    verify(authService, Mockito.times(1)).confirmRegistration(TEST_CONFIRM_REGISTRATION_DTO);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void Login_Should_Return_CREATED() {
    ResponseEntity responseEntity =
        authControllerImpl.login(AuthUtils.TEST_LOGIN_DTO, TEST_REQUEST);

    verify(authService, Mockito.times(1)).login(TEST_LOGIN_DTO, TEST_REQUEST);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception() {
    when(authService.login(TEST_LOGIN_DTO, TEST_REQUEST)).thenThrow(BadCredentialsException.class);
    authControllerImpl.login(AuthUtils.TEST_LOGIN_DTO, TEST_REQUEST);
  }

  @Test
  public void Logout_Should_Return_NO_CONTENT() {
    ResponseEntity responseEntity = authControllerImpl.logout(TEST_REQUEST, TEST_RESPONSE);
    verify(authService, Mockito.times(1)).logout(TEST_REQUEST, TEST_RESPONSE);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  @Test
  public void FinalizeEmailChange_Should_CallAuthService() {
    ResponseEntity responseEntity =
        authControllerImpl.finalizeEmailChange(TEST_CONFIRM_EMAIL_CHANGE_DTO);
    verify(authService, times(1)).finalizeEmailChange(TEST_CONFIRM_EMAIL_CHANGE_DTO);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
