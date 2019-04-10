package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.CampaignTopicDto;
import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicType;

public class CampaignTopicUtil {

  public static final String TEST_CAMPAIGN_TOPIC_CONTENT = "TEST_CAMPAIGN_TOPIC_CONTENT";
  public static final String TEST_CAMPAIGN_TOPIC_TYPE_NAME = "TEST_CAMPAIGN_TOPIC_TYPE_NAME";
  public static final CampaignTopicType TEST_CAMPAIGN_TOPIC_TYPE =
      CampaignTopicType.builder().name(TEST_CAMPAIGN_TOPIC_TYPE_NAME).build();
  public static final CampaignTopic TEST_CAMPAIGN_TOPIC =
      CampaignTopic.builder()
          .content(TEST_CAMPAIGN_TOPIC_CONTENT)
          .campaignTopicType(TEST_CAMPAIGN_TOPIC_TYPE)
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .build();

  public static final CampaignTopicDto TEST_CAMPAIGN_TOPIC_DTO =
      new CampaignTopicDto(TEST_CAMPAIGN_TOPIC_CONTENT);
}
