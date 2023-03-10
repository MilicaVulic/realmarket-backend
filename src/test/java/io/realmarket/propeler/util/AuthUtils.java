package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.AuthStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.security.UserAuthentication;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

import static io.realmarket.propeler.service.util.RememberMeCookieHelper.COOKIE_NAME;
import static io.realmarket.propeler.util.RememberMeCookieUtils.TEST_VALUE;

public class AuthUtils {

  public static final String TEST_USERNAME = "TEST_USERNAME";
  public static final String TEST_EMAIL = "TEST_EMAIL";
  public static final String TEST_PASSWORD = "TEST_PASSWORD";
  public static final String TEST_PASSWORD_NEW = "TEST_PASSWORD_NEW";
  public static final String TEST_ENCODED_SECRET = "enc_secret";
  public static final String TEST_TEMPORARY_TOKEN_VALUE = "TEST_TEMPORARY_TOKEN_VALUE";
  public static final Long TEST_AUTH_ID = 10L;
  public static final Long TEST_AUTH_ADMIN_ID = TEST_AUTH_ID + 3;

  public static final ChangePasswordDto TEST_CHANGE_PASSWORD_DTO =
      ChangePasswordDto.builder().oldPassword(TEST_PASSWORD).newPassword(TEST_PASSWORD_NEW).build();
  public static final PasswordDto TEST_PASSWORD_DTO = new PasswordDto(TEST_PASSWORD);
  public static final UsernameDto TEST_USERNAME_DTO = new UsernameDto(TEST_USERNAME);
  public static final ConfirmRegistrationDto TEST_CONFIRM_REGISTRATION_DTO =
      ConfirmRegistrationDto.builder().token(TEST_TEMPORARY_TOKEN_VALUE).build();
  public static final LoginDto TEST_LOGIN_DTO =
      LoginDto.builder().password(TEST_PASSWORD).username(TEST_USERNAME).build();
  public static final ResetPasswordDto TEST_RESET_PASSWORD_DTO =
      new ResetPasswordDto(TEST_TEMPORARY_TOKEN_VALUE, TEST_PASSWORD_NEW);
  private static final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
  private static final String TEST_LAST_NAME = "TEST_LAST_NAME";

  public static final UserRoleName TEST_ROLE_ADMIN = UserRoleName.ROLE_ADMIN;
  public static final UserRoleName TEST_ROLE_INDIVIDUAL_INVESTOR =
      UserRoleName.ROLE_INDIVIDUAL_INVESTOR;
  public static final UserRoleName TEST_ROLE_CORPORATE_INVESTOR =
      UserRoleName.ROLE_CORPORATE_INVESTOR;
  public static final UserRoleName TEST_ROLE_ENTREPRENEUR = UserRoleName.ROLE_ENTREPRENEUR;

  public static final UserRole TEST_ADMIN_ROLE = UserRole.builder().name(TEST_ROLE_ADMIN).build();
  public static final UserRole TEST_INDIVIDUAL_INVESTOR_USER_ROLE =
      UserRole.builder().name(TEST_ROLE_INDIVIDUAL_INVESTOR).build();
  public static final UserRole TEST_CORPORATE_INVESTOR_USER_ROLE =
      UserRole.builder().name(TEST_ROLE_CORPORATE_INVESTOR).build();
  public static final UserRole TEST_ENTREPRENEUR_USER_ROLE =
      UserRole.builder().name(TEST_ROLE_ENTREPRENEUR).build();

  public static final AuthState TEST_AUTH_STATE =
      AuthState.builder().name(AuthStateName.ACTIVE).id(100L).build();

  public static final String TEST_COUNTRY_CODE = "RS";
  public static final Country TEST_COUNTRY = Country.builder().code("RS").name("Serbia").build();
  public static final String TEST_COUNTRY_CODE2 = "BS";
  public static final Country TEST_COUNTRY2 = Country.builder().code("BS").name("Bahamas").build();

  public static final EntrepreneurRegistrationDto TEST_ENTREPRENEUR_REGISTRATION_DTO =
      EntrepreneurRegistrationDto.entrepreneurRegistrationDtoBuilder()
          .registrationToken(RegistrationTokenUtils.TEST_VALUE)
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence(TEST_COUNTRY_CODE)
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .build();

  public static final RegistrationDto TEST_REGISTRATION_DTO =
      RegistrationDto.builder()
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence("RS")
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .build();

  public static final CorporateInvestorRegistrationDto TEST_CORPORATE_INVESTOR_REGISTRATION_DTO =
      CorporateInvestorRegistrationDto.corporateInvestorRegistrationDtoBuilder()
          .email(TEST_EMAIL)
          .username(TEST_USERNAME)
          .password(TEST_PASSWORD)
          .firstName(TEST_FIRST_NAME)
          .lastName(TEST_LAST_NAME)
          .countryOfResidence("RS")
          .city("TEST_CITY")
          .address("TEST_ADDRESS")
          .companyName("TEST_COMPANY")
          .companyIdentificationNumber("1234")
          .build();

  public static final Auth TEST_AUTH =
      Auth.builder()
          .id(TEST_AUTH_ID)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_INDIVIDUAL_INVESTOR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(PersonUtils.TEST_PERSON)
          .blocked(false)
          .build();

  public static final Auth TEST_AUTH2 =
      Auth.builder()
          .id(TEST_AUTH_ID + 1)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_INDIVIDUAL_INVESTOR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO, TEST_COUNTRY, null))
          .build();

  public static final Auth TEST_AUTH_ENTREPRENEUR =
      Auth.builder()
          .id(TEST_AUTH_ID)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_ENTREPRENEUR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO, TEST_COUNTRY, null))
          .build();

  public static final Auth TEST_AUTH_INDIVIDUAL_INVESTOR =
      Auth.builder()
          .id(TEST_AUTH_ID + 1)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_INDIVIDUAL_INVESTOR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO, TEST_COUNTRY, null))
          .build();

  public static final Auth TEST_AUTH_CORPORATE_INVESTOR =
      Auth.builder()
          .id(TEST_AUTH_ID + 2)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_CORPORATE_INVESTOR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO, TEST_COUNTRY, null))
          .build();

  public static final Auth TEST_AUTH_ADMIN =
      Auth.builder()
          .id(TEST_AUTH_ID + 3)
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_ADMIN_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(TEST_ENCODED_SECRET)
          .person(new Person(TEST_REGISTRATION_DTO, TEST_COUNTRY, null))
          .build();

  public static final Auth TEST_AUTH_OLD_SECRET =
      Auth.builder()
          .username(TEST_USERNAME)
          .state(TEST_AUTH_STATE)
          .userRole(TEST_INDIVIDUAL_INVESTOR_USER_ROLE)
          .password(TEST_PASSWORD)
          .totpSecret(OTPUtils.TEST_SECRET_1)
          .person(new Person(TEST_REGISTRATION_DTO, TEST_COUNTRY, null))
          .build();

  public static final ConfirmEmailChangeDto TEST_CONFIRM_EMAIL_CHANGE_DTO =
      ConfirmEmailChangeDto.builder().token(TEST_TEMPORARY_TOKEN_VALUE).build();
  public static final UserAuthentication TEST_USER_AUTH =
      new UserAuthentication(TEST_AUTH.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final UserAuthentication TEST_USER_AUTH2 =
      new UserAuthentication(TEST_AUTH2.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final UserAuthentication TEST_ENTREPRENEUR_USER_AUTH =
      new UserAuthentication(
          TEST_AUTH_ENTREPRENEUR.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final UserAuthentication TEST_ADMIN_AUTH =
      new UserAuthentication(TEST_AUTH_ADMIN.toBuilder().build(), TEST_TEMPORARY_TOKEN_VALUE);
  public static final MockHttpServletRequest TEST_REQUEST = new MockHttpServletRequest();
  public static final MockHttpServletResponse TEST_RESPONSE = new MockHttpServletResponse();
  public static final Cookie TEST_COOKIE = new Cookie(COOKIE_NAME, TEST_VALUE);

  static {
    TEST_REQUEST.setCookies(TEST_COOKIE);
  }

  public static void mockSecurityContext(UserAuthentication userAuthentication) {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(userAuthentication);
    SecurityContextHolder.setContext(securityContext);
  }

  public static void mockRequestAndContext() {
    mockSecurityContext(TEST_USER_AUTH);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Forwarded-For", "localhost");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  public static void mockRequestAndContextEntrepreneur() {
    mockSecurityContext(TEST_ENTREPRENEUR_USER_AUTH);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Forwarded-For", "localhost");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  public static void mockRequestAndContextAdmin() {
    mockSecurityContext(TEST_ADMIN_AUTH);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Forwarded-For", "localhost");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }
}
