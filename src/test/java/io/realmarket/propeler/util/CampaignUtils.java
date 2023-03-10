package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignClosingReasonDto;
import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

import java.math.BigDecimal;

public class CampaignUtils {

  public static final Long TEST_CAMPAIGN_ID = 1L;
  public static final String TEST_URL_FRIENDLY_NAME = "TEST_URL_FRIENDLY_NAME";
  public static final String TEST_ACTIVE_URL_FRIENDLY_NAME = "TEST_ACTIVE_URL_FRIENDLY_NAME";
  public static final String TEST_MARKET_IMAGE_UTL = "MARKET_IMAGE_URL";
  public static final Long TEST_FUNDING_GOALS = 100000L;
  public static final String TEST_CURRENCY = "EUR";
  public static final BigDecimal TEST_MIN_EQUITY_OFFERED = BigDecimal.valueOf(5);
  public static final BigDecimal TEST_MAX_EQUITY_OFFERED = BigDecimal.valueOf(10);
  public static final BigDecimal TEST_MIN_INVESTMENT = BigDecimal.valueOf(600);
  public static final String TEST_CLOSING_REASON = "TEST_CLOSING_REASON";
  public static final CampaignState TEST_CAMPAIGN_INITIAL_STATE =
      CampaignState.builder().name(CampaignStateName.INITIAL).build();
  public static final CampaignState TEST_REVIEW_READY_CAMPAIGN_STATE =
      CampaignState.builder().name(CampaignStateName.REVIEW_READY).build();
  public static final CampaignState TEST_AUDIT_CAMPAIGN_STATE =
      CampaignState.builder().name(CampaignStateName.AUDIT).build();
  public static final CampaignState TEST_LAUNCH_READY_CAMPAIGN_STATE =
      CampaignState.builder().name(CampaignStateName.LAUNCH_READY).build();
  public static final CampaignState TEST_CAMPAIGN_ACTIVE_STATE =
      CampaignState.builder().name(CampaignStateName.ACTIVE).build();
  public static final CampaignState TEST_CAMPAIGN_SUCCESSFUL_STATE =
      CampaignState.builder().name(CampaignStateName.SUCCESSFUL).build();
  public static final CampaignState TEST_CAMPAIGN_UNSUCCESSFUL_STATE =
      CampaignState.builder().name(CampaignStateName.UNSUCCESSFUL).build();
  public static final String TEST_NAME = "TEST_NAME";
  public static final CampaignState TEST_CAMPAIGN_DELETED_STATE =
      CampaignState.builder().name(CampaignStateName.DELETED).build();
  public static final String TEST_TAG_LINE = "TAG_LINE";
  public static final CampaignState TEST_CAMPAIGN_AUDIT_STATE =
      CampaignState.builder().name(CampaignStateName.AUDIT).build();

  public static CampaignState mockCampaignState(CampaignStateName name) {
    return CampaignState.builder().name(name).build();
  }

  public static final Campaign TEST_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .name(TEST_NAME)
          .fundingGoals(TEST_FUNDING_GOALS)
          .currency(TEST_CURRENCY)
          .minEquityOffered(TEST_MIN_EQUITY_OFFERED)
          .maxEquityOffered(TEST_MAX_EQUITY_OFFERED)
          .minInvestment(TEST_MIN_INVESTMENT)
          .campaignState(TEST_CAMPAIGN_INITIAL_STATE)
          .tagLine(TEST_TAG_LINE)
          .build();

  public static final Campaign TEST_AUDIT_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .name(TEST_NAME)
          .fundingGoals(TEST_FUNDING_GOALS)
          .currency(TEST_CURRENCY)
          .minEquityOffered(TEST_MIN_EQUITY_OFFERED)
          .maxEquityOffered(TEST_MAX_EQUITY_OFFERED)
          .minInvestment(TEST_MIN_INVESTMENT)
          .campaignState(TEST_CAMPAIGN_AUDIT_STATE)
          .tagLine(TEST_TAG_LINE)
          .build();

  public static final CampaignDto TEST_CAMPAIGN_DTO = new CampaignDto(TEST_CAMPAIGN);

  public static Campaign getCampaignMocked() {
    return Campaign.builder()
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .fundingGoals(TEST_FUNDING_GOALS)
        .currency(TEST_CURRENCY)
        .minEquityOffered(TEST_MIN_EQUITY_OFFERED)
        .maxEquityOffered(TEST_MAX_EQUITY_OFFERED)
        .minInvestment(TEST_MIN_INVESTMENT)
        .campaignState(TEST_CAMPAIGN_INITIAL_STATE)
        .marketImageUrl(TEST_MARKET_IMAGE_UTL)
        .build();
  }

  public static final Campaign TEST_REVIEW_READY_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .name(TEST_NAME)
          .fundingGoals(TEST_FUNDING_GOALS)
          .currency(TEST_CURRENCY)
          .minEquityOffered(TEST_MIN_EQUITY_OFFERED)
          .maxEquityOffered(TEST_MAX_EQUITY_OFFERED)
          .minInvestment(TEST_MIN_INVESTMENT)
          .campaignState(TEST_REVIEW_READY_CAMPAIGN_STATE)
          .tagLine(TEST_TAG_LINE)
          .build();

  public static final Campaign TEST_READY_FOR_LAUNCH_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .name(TEST_NAME)
          .fundingGoals(TEST_FUNDING_GOALS)
          .currency(TEST_CURRENCY)
          .minEquityOffered(TEST_MIN_EQUITY_OFFERED)
          .maxEquityOffered(TEST_MAX_EQUITY_OFFERED)
          .minInvestment(TEST_MIN_INVESTMENT)
          .campaignState(TEST_LAUNCH_READY_CAMPAIGN_STATE)
          .tagLine(TEST_TAG_LINE)
          .build();

  public static final Campaign TEST_ACTIVE_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_ACTIVE_URL_FRIENDLY_NAME)
          .fundingGoals(0L)
          .currency(TEST_CURRENCY)
          .campaignState(TEST_CAMPAIGN_ACTIVE_STATE)
          .build();

  public static final Campaign TEST_SUCCESSFUL_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_ACTIVE_URL_FRIENDLY_NAME)
          .fundingGoals(0L)
          .currency(TEST_CURRENCY)
          .campaignState(TEST_CAMPAIGN_SUCCESSFUL_STATE)
          .build();

  public static final Campaign TEST_UNSUCCESSFUL_CAMPAIGN =
      Campaign.builder()
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_ACTIVE_URL_FRIENDLY_NAME)
          .fundingGoals(0L)
          .currency(TEST_CURRENCY)
          .campaignState(TEST_CAMPAIGN_UNSUCCESSFUL_STATE)
          .build();

  public static Campaign getActiveCampaignMocked() {
    return Campaign.builder()
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .fundingGoals(TEST_FUNDING_GOALS)
        .currency(TEST_CURRENCY)
        .minEquityOffered(TEST_MIN_EQUITY_OFFERED)
        .maxEquityOffered(TEST_MAX_EQUITY_OFFERED)
        .minInvestment(TEST_MIN_INVESTMENT)
        .campaignState(TEST_CAMPAIGN_ACTIVE_STATE)
        .marketImageUrl(TEST_MARKET_IMAGE_UTL)
        .build();
  }

  public static final Campaign TEST_INVESTABLE_CAMPAIGN =
      Campaign.builder()
          .id(1L)
          .company(CompanyUtils.getCompanyMocked())
          .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
          .name(TEST_NAME)
          .fundingGoals(100L)
          .currency(TEST_CURRENCY)
          .minInvestment(BigDecimal.ONE)
          .collectedAmount(BigDecimal.TEN)
          .minEquityOffered(BigDecimal.ONE)
          .maxEquityOffered(BigDecimal.TEN)
          .campaignState(TEST_CAMPAIGN_ACTIVE_STATE)
          .tagLine(TEST_TAG_LINE)
          .build();

  public static Campaign getInvestableCampaignMocked() {
    return Campaign.builder()
        .id(1L)
        .company(CompanyUtils.getCompanyMocked())
        .urlFriendlyName(TEST_URL_FRIENDLY_NAME)
        .name(TEST_NAME)
        .fundingGoals(100L)
        .currency(TEST_CURRENCY)
        .minInvestment(BigDecimal.ONE)
        .collectedAmount(BigDecimal.TEN)
        .minEquityOffered(BigDecimal.ONE)
        .maxEquityOffered(BigDecimal.TEN)
        .campaignState(TEST_CAMPAIGN_ACTIVE_STATE)
        .tagLine(TEST_TAG_LINE)
        .build();
  }

  public static final CampaignClosingReasonDto TEST_CAMPAIGN_CLOSING_REASON_SUCCESSFUL_DTO =
      new CampaignClosingReasonDto(TEST_CLOSING_REASON, true);

  public static final CampaignClosingReasonDto TEST_CAMPAIGN_CLOSING_REASON_UNSUCCESSFUL_DTO =
      new CampaignClosingReasonDto(TEST_CLOSING_REASON, false);

  public static CampaignPatchDto TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS() {
    return CampaignPatchDto.builder().fundingGoals(TEST_FUNDING_GOALS).build();
  }
}
