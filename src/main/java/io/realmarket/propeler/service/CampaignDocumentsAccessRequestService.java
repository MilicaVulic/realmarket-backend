package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignDocumentsAccessRequestsDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;

public interface CampaignDocumentsAccessRequestService {

  CampaignDocumentsAccessRequest sendCampaignDocumentsAccessRequest(String campaignName);

  String getCampaignDocumentsAccessRequestStatus(String campaignName);

  CampaignDocumentsAccessRequestsDto getCampaignDocumentsAccessRequests();

  CampaignDocumentsAccessRequest acceptCampaignDocumentsAccessRequest(Long requestId);

  CampaignDocumentsAccessRequest rejectCampaignDocumentsAccessRequest(Long requestId);

  boolean hasCampaignDocumentsAccessRequest(Campaign campaign);
}
