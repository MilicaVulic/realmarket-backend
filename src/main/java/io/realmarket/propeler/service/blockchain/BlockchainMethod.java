package io.realmarket.propeler.service.blockchain;

public class BlockchainMethod {
  public static final String USER_REGISTRATION = "UserRegistration";
  public static final String USER_PASSWORD_CHANGE = "UserPasswordChange";
  public static final String USER_EMAIL_CHANGE = "UserEmailChange";
  public static final String USER_REGENERATION_OF_RECOVERY = "UserRegenerationOfRecovery";
  public static final String USER_KYC_REQUEST_FOR_REVIEW = "UserKYCRequestForReview";
  public static final String USER_KYC_STATE_CHANGE = "UserKYCStateChange";
  public static final String CAMPAIGN_SUBMISSION_FOR_REVIEW = "CampaignSubmissionForReview";
  public static final String COMPANY_REGISTRATION = "CompanyRegistration";
  public static final String COMPANY_EDIT_REQUEST = "CompanyEditRequest";
  public static final String CAMPAIGN_STATE_CHANGE = "CampaignStateChange";
  public static final String CAMPAIGN_DOCUMENT_ACCESS_REQUEST = "CampaignDocumentAccessRequest";
  public static final String CAMPAIGN_DOCUMENT_ACCESS_REQUEST_STATE_CHANGE =
      "CampaignDocumentAccessRequestStateChange";
  public static final String SUBMIT_SHAREHOLDERS = "CompanyShareholderSubmission";
  public static final String INVESTMENT_INTENT = "CampaignInvestmentIntent";
  public static final String INVESTMENT_STATE_CHANGE = "CampaignInvestmentStateChange";
  public static final String PAYMENT_CONFIRMED = "CampaignInvestmentPaymentConfirmed";
  public static final String CAMPAIGN_CLOSING = "CampaignClosing";

  private BlockchainMethod() {}
}
