package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.service.TwoFactorAuthService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.realmarket.propeler.util.AuthUtils.TEST_RESPONSE;
import static io.realmarket.propeler.util.TwoFactorAuthUtils.LOGIN_2F_DTO_RM;
import static io.realmarket.propeler.util.TwoFactorAuthUtils.TEST_LOGIN_RESPONSE_DTO;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class TwoFactorAuthControllerImplTest {

  @InjectMocks private TwoFactorAuthControllerImpl twoFactorAuthController;

  @Mock private TwoFactorAuthService twoFactorAuthService;

  @Test
  public void Login2FA_Should_Return_LoginResponseDto() {
    when(twoFactorAuthService.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE))
        .thenReturn(TEST_LOGIN_RESPONSE_DTO);

    ResponseEntity<LoginResponseDto> retVal =
        twoFactorAuthController.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE);

    assertEquals(HttpStatus.OK, retVal.getStatusCode());
    assertEquals(TEST_LOGIN_RESPONSE_DTO, retVal.getBody());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void Login2FA_Should_Return_Forbidden() {
    when(twoFactorAuthService.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE))
        .thenThrow(ForbiddenOperationException.class);

    ResponseEntity<LoginResponseDto> retVal =
        twoFactorAuthController.login2FA(LOGIN_2F_DTO_RM, TEST_RESPONSE);

    assertEquals(HttpStatus.FORBIDDEN, retVal.getStatusCode());
  }
}
