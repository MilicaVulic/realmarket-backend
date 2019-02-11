package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.security.UserAuthentication;
import io.realmarket.propeler.service.impl.EmailServiceImpl;
import io.realmarket.propeler.service.util.MailContentHolder;

import java.util.HashMap;

import static io.realmarket.propeler.unit.util.PersonUtils.getMockedPerson;

public class AuthUtils {
  public static final String TEST_USERNAME = "TEST_USERNAME";
  public static final String TEST_EMAIL = "TEST_EMAIL";
  public static final String TEST_PASSWORD = "TEST_PASSWORD";
  public static final String TEST_PASSWORD_NEW = "TEST_PASSWORD_NEW";
  public static final EUserRole TEST_ROLE = EUserRole.ROLE_INVESTOR;
  public static final EUserRole TEST_ROLE_FORBIDDEN = EUserRole.ROLE_ADMIN;
  public static final String TEST_TEMPORARY_TOKEN_VALUE = "TEST_TEMPORARY_TOKEN_VALUE";
  public static final Long TEST_AUTH_ID = 10L;

  private static final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
  public static final MailContentHolder TEST_EMAIL_DTO =
      new MailContentHolder(
          TEST_EMAIL,
          EEmailType.REGISTER,
          new HashMap<String, Object>() {
            {
              put(EmailServiceImpl.USERNAME, TEST_USERNAME);
              put(EmailServiceImpl.ACTIVATION_TOKEN, TEST_TEMPORARY_TOKEN_VALUE);
            }
          });
  public static final ChangePasswordDto TEST_CHANGE_PASSWORD_DTO =
      ChangePasswordDto.builder().oldPassword(TEST_PASSWORD).newPassword(TEST_PASSWORD_NEW).build();

  public static final UsernameDto TEST_USERNAME_DTO = new UsernameDto(TEST_USERNAME);
  public static final ConfirmRegistrationDto TEST_CONFIRM_REGISTRATION_DTO =
      ConfirmRegistrationDto.builder().token(TEST_TEMPORARY_TOKEN_VALUE).build();

  public static final LoginDto TEST_LOGIN_DTO =
      LoginDto.builder().password(TEST_PASSWORD).username(TEST_USERNAME).build();
  public static final ResetPasswordDto TEST_RESET_PASSWORD_DTO =
      new ResetPasswordDto(TEST_TEMPORARY_TOKEN_VALUE, TEST_PASSWORD_NEW);
  private static final String TEST_LAST_NAME = "TEST_LAST_NAME";
  public static final RegistrationDto TEST_REGISTRATION_DTO =
      RegistrationDto.builder()
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .userRole(TEST_ROLE)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence("TEST_COUNTRY_OF_RESIDENCE")
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .build();
  public static final RegistrationDto TEST_REGISTRATION_DTO_ROLE_NOT_ALLOWED =
      RegistrationDto.builder()
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .userRole(TEST_ROLE_FORBIDDEN)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence("TEST_COUNTRY_OF_RESIDENCE")
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .build();

  public static final Auth TEST_AUTH =
          Auth.builder()
                  .username(TEST_USERNAME)
                  .active(false)
                  .userRole(TEST_ROLE)
                  .password(TEST_PASSWORD)
                  .person(getMockedPerson(TEST_REGISTRATION_DTO))
                  .build();

  public static final UserAuthentication TEST_USER_AUTH =
          new UserAuthentication(TEST_AUTH, TEST_TEMPORARY_TOKEN_VALUE);
}
