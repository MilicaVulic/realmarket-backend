package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentsAccessRequestDto;
import io.realmarket.propeler.api.dto.CampaignDocumentsAccessRequestsDto;
import io.realmarket.propeler.api.dto.CampaignResponseDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.NotificationType;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CampaignDocumentsAccessRequestRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.campaign.CampaignDocumentAccessRequestStateChangeDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignDocumentsAccessRequestServiceImpl
    implements CampaignDocumentsAccessRequestService {

  private final CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository;
  private final CampaignDocumentService campaignDocumentService;
  private final CampaignService campaignService;
  private final AuthService authService;
  private final RequestStateService requestStateService;
  private final NotificationService notificationService;
  private final BlockchainMessageProducer blockchainMessageProducer;

  @Autowired
  public CampaignDocumentsAccessRequestServiceImpl(
      CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository,
      @Lazy CampaignDocumentService campaignDocumentService,
      CampaignService campaignService,
      AuthService authService,
      RequestStateService requestStateService,
      NotificationService notificationService,
      BlockchainMessageProducer blockchainMessageProducer) {
    this.campaignDocumentsAccessRequestRepository = campaignDocumentsAccessRequestRepository;
    this.campaignDocumentService = campaignDocumentService;
    this.campaignService = campaignService;
    this.authService = authService;
    this.requestStateService = requestStateService;
    this.blockchainMessageProducer = blockchainMessageProducer;
    this.notificationService = notificationService;
  }

  private CampaignDocumentsAccessRequest findByIdOrThrowException(Long requestId) {
    return campaignDocumentsAccessRequestRepository
        .findById(requestId)
        .orElseThrow(EntityNotFoundException::new);
  }

  private CampaignDocumentsAccessRequest findByCampaignAndAuth(Campaign campaign, Auth auth) {
    return campaignDocumentsAccessRequestRepository.findByCampaignAndAuth(campaign, auth);
  }

  private List<CampaignDocumentsAccessRequest> findByCampaign(Campaign campaign) {
    return campaignDocumentsAccessRequestRepository.findByCampaign(campaign);
  }

  private CampaignDocumentsAccessRequest findByCampaignAndAuthAndApproved(
      Campaign campaign, Auth auth) {
    RequestState requestState = requestStateService.getRequestState(RequestStateName.APPROVED);
    return campaignDocumentsAccessRequestRepository.findByCampaignAndAuthAndRequestState(
        campaign, auth, requestState);
  }

  @Override
  public CampaignDocumentsAccessRequest sendCampaignDocumentsAccessRequest(String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    auth = authService.findByIdOrThrowException(auth.getId());

    CampaignDocumentsAccessRequest campaignDocumentsAccessRequest =
        campaignDocumentsAccessRequestRepository.save(
            CampaignDocumentsAccessRequest.builder()
                .campaign(campaign)
                .auth(auth)
                .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
                .build());

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.CAMPAIGN_DOCUMENT_ACCESS_REQUEST,
        new io.realmarket.propeler.service.blockchain.dto.campaign.CampaignDocumentAccessRequestDto(
            campaignDocumentsAccessRequest, auth.getId()),
        auth.getUsername(),
        AuthenticationUtil.getClientIp());

    return campaignDocumentsAccessRequest;
  }

  @Override
  public CampaignDocumentsAccessRequestsDto getCampaignDocumentsAccessRequests() {
    Campaign activeCampaign = campaignService.getActiveCampaign();

    return getRequestsForCampaign(activeCampaign);
  }

  @Override
  public String getCampaignDocumentsAccessRequestStatus(String campaignName) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignName);
    if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();

    CampaignDocumentsAccessRequest campaignDocumentsAccessRequest =
        findByCampaignAndAuth(campaign, auth);
    if (campaignDocumentsAccessRequest != null) {
      return campaignDocumentsAccessRequest.getRequestState().getName().toString();
    }

    List<CampaignDocument> campaignDocuments =
        campaignDocumentService.findAllByCampaignOrderByUploadDateDesc(campaign);
    // TODO: Discuss about these messages
    for (CampaignDocument campaignDocument : campaignDocuments) {
      if (campaignDocument.getAccessLevel().getName().equals(DocumentAccessLevelName.ON_DEMAND)) {
        return "NEED REQUEST";
      }
    }
    return "NO ON DEMAND DOCUMENTS";
  }

  private CampaignDocumentsAccessRequestsDto getRequestsForCampaign(Campaign campaign) {
    CampaignDocumentsAccessRequestsDto campaignDocumentsAccessRequestsDto =
        new CampaignDocumentsAccessRequestsDto();
    campaignDocumentsAccessRequestsDto.setCampaign(new CampaignResponseDto(campaign));

    List<CampaignDocumentsAccessRequestDto> requestList =
        findByCampaign(campaign).stream()
            .map(CampaignDocumentsAccessRequestDto::new)
            .collect(Collectors.toList());
    campaignDocumentsAccessRequestsDto.setRequests(requestList);

    return campaignDocumentsAccessRequestsDto;
  }

  @Override
  public CampaignDocumentsAccessRequest acceptCampaignDocumentsAccessRequest(Long requestId) {
    CampaignDocumentsAccessRequest campaignDocumentsAccessRequest =
        findByIdOrThrowException(requestId);
    campaignService.throwIfNoAccess(campaignDocumentsAccessRequest.getCampaign());
    campaignDocumentsAccessRequest.setRequestState(
        requestStateService.getRequestState(RequestStateName.APPROVED));
    notificationService.sendMessage(
        campaignDocumentsAccessRequest.getAuth(),
        NotificationType.ACCEPT_DOCUMENTS,
        null,
        campaignDocumentsAccessRequest.getCampaign().getName());
    return saveAndSendToBlockchain(campaignDocumentsAccessRequest);
  }

  @Override
  public CampaignDocumentsAccessRequest rejectCampaignDocumentsAccessRequest(Long requestId) {
    CampaignDocumentsAccessRequest campaignDocumentsAccessRequest =
        findByIdOrThrowException(requestId);
    campaignService.throwIfNoAccess(campaignDocumentsAccessRequest.getCampaign());
    campaignDocumentsAccessRequest.setRequestState(
        requestStateService.getRequestState(RequestStateName.DECLINED));
    notificationService.sendMessage(
        campaignDocumentsAccessRequest.getAuth(),
        NotificationType.REJECT_DOCUMENTS,
        null,
        campaignDocumentsAccessRequest.getCampaign().getName());
    return saveAndSendToBlockchain(campaignDocumentsAccessRequest);
  }

  private CampaignDocumentsAccessRequest saveAndSendToBlockchain(
      CampaignDocumentsAccessRequest campaignDocumentsAccessRequest) {
    campaignDocumentsAccessRequest =
        campaignDocumentsAccessRequestRepository.save(campaignDocumentsAccessRequest);

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.CAMPAIGN_DOCUMENT_ACCESS_REQUEST_STATE_CHANGE,
        new CampaignDocumentAccessRequestStateChangeDto(
            campaignDocumentsAccessRequest,
            AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return campaignDocumentsAccessRequest;
  }

  @Override
  public boolean hasCampaignDocumentsAccessRequest(Campaign campaign) {
    Auth auth = AuthenticationUtil.getAuthOrReturnNull();
    return (auth != null) && findByCampaignAndAuthAndApproved(campaign, auth) != null;
  }
}
