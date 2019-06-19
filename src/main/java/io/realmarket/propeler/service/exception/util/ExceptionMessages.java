package io.realmarket.propeler.service.exception.util;

public interface ExceptionMessages {

  String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials provided";

  String USERNAME_ALREADY_EXISTS = "Person with the provided username already exists!";

  String USERNAME_DOES_NOT_EXISTS = "Person with provided username does not exist.";

  String EMAIL_DOES_NOT_EXIST = "Person with provided e-mail address does not exist!";

  String PERSON_ID_DOES_NOT_EXISTS = "Person with provided identifier does not exist.";

  String INVALID_REQUEST = "Invalid request!";

  String EMAIL_SENDING_EXCEPTION = "Email Sending exception: ";

  String INVALID_TOKEN_PROVIDED = "Invalid token provided";

  String INVALID_TOKEN_TYPE = "Invalid token type";

  String INVALID_TOTP_CODE_PROVIDED = "Invalid TOTP code provided";

  String PROFILE_PICTURE_DOES_NOT_EXIST = "Profile picture does not exist!";

  String IMAGE_DOES_NOT_EXIST = "Image does not exist!";

  String FORBIDDEN_OPERATION_EXCEPTION = "Not allowed operation";

  String AUTHORIZATION_ACTION_NOT_FOUND = "Authorization action not found";

  String CAMPAIGN_NOT_FOUND = "Campaign with the provided name not found!";

  String CAMPAIGN_STATE_NOT_FOUND = "Campaign state with the provided name not found!";

  String REQUEST_STATE_NOT_FOUND = "Request state with the provided name not found!";

  String INVESTMENT_STATE_NOT_FOUND = "Investment state with the provided name not found!";

  String SHAREHOLDER_NOT_FOUND = "Shareholder with the provided id not found!";

  String USER_IS_NOT_OWNER_OF_CAMPAIGN = "Caller is not owner of campaign.";

  String USER_IS_NOT_OWNER_OF_COMPANY = "Caller is not owner of company.";

  String TEAM_MEMBER_NOT_FOUND = "Campaign teem member with the provided id not found!";

  String CAMPAIGN_NAME_ALREADY_EXISTS = "Campaign with the provided name already exists!";

  String INVALID_CAPTCHA = "Captcha did not validate successfully!";

  String BLOCKED_CLIENT = "Maximum number of failed attempts exceeded, try again in half an hour";

  String NOT_COMPANY_OWNER = "User is not an owner of a company!";

  String COMPANY_ALREADY_EXIST = "Company already exists!";

  String COMPANY_DOES_NOT_EXIST = "Company with provided id does not exist.";

  String ENTREPRENEUR_MISSING_COMPANY = "Entrepreneur does not have a company.";

  String FILE_NOT_EXISTS = "File with provided URL does not exist!";

  String NO_ACTIVE_CAMPAIGN = "No active campaign";

  String CAMPAIGN_TOPIC_TYPE_NOT_EXISTS =
      "Campaign topic type with the provided name does not exist!";

  String ACTIVE_CAMPAIGN_EXISTS = "Active campaign already exists for given company";

  String CAMPAIGN_TOPIC_NOT_EXISTS =
      "Campaign topic for the provided campaign name and campaign topic type not found!";

  String PLATFORM_SETTINGS_NOT_FOUND = "Platform settings couldn't be found!";

  String INVESTMENT_MUST_BE_GREATER_THAN_PLATFORM_MIN =
      "Campaign investment must be greater than platform minimum!";

  String CAMPAIGN_NOT_EDITABLE = "Campaign can not be edited at this stage";

  String INVALID_COUNTRY_CODE = "Invalid country code";

  String INVESTMENT_MUST_BE_GREATER_THAN_CAMPAIGN_MIN_INVESTMENT =
      "Amount of money must be greater than campaign minimum investment.";

  String INVESTMENT_CAN_NOT_BE_GREATER_THAN_MAX_INVESTMENT =
      "Amount of money can not be greater than maximum investment.";

  String INVESTMENT_CAN_NOT_BE_GREATER_THAN_CAMPAIGN_MAXIMUM_EQUITY =
      "Percentage of equity can not be greater than campaign maximum equity offered.";

  String NEGATIVE_VALUE_EXCEPTION = "Negative value provided.";

  String ROLE_NOT_FOUND = "Role with the provided name not found!";

  String NOT_CAMPAIGN_INVESTOR = "User is not investor in this campaign!";

  String CAMPAIGN_IS_NOT_ACTIVE = "Campaign is not active!";

  String INVESTMENT_CAN_NOT_BE_REVOKED = "Investment can not be revoked at this stage!";

  String INVESTMENT_CAN_BE_REVOKED = "Investment can be revoked at this stage!";

  String PAYMENT_NOT_PROCESSED = "Payment not processed for this investment!";
}
