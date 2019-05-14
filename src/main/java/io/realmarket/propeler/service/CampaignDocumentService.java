package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.model.CampaignDocument;

import java.util.List;
import java.util.Map;

public interface CampaignDocumentService {

  CampaignDocument submitDocument(
      CampaignDocument campaignDocument, String campaignUrlFriendlyName);

  void deleteDocument(String campaignUrlFriendlyName, Long documentId);

  Map<String, List<CampaignDocumentDto>> getAllCampaignDocumentDtoGropedByType(String campaignName);

  CampaignDocument findByIdOrThrowException(Long documentId);
}
