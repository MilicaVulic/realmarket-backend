package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.service.CampaignTopicService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.exception.ActiveCampaignAlreadyExistsException;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.impl.CampaignServiceImpl;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.unit.util.CampaignUtils;
import io.realmarket.propeler.unit.util.CompanyUtils;
import io.realmarket.propeler.unit.util.FileUtils;
import io.realmarket.propeler.unit.util.PlatformSettingsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.CampaignUtils.*;
import static io.realmarket.propeler.unit.util.CompanyUtils.TEST_FEATURED_IMAGE_URL;
import static io.realmarket.propeler.unit.util.CompanyUtils.getCompanyMocked;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignServiceImpl.class)
public class CampaignServiceImplTest {
  @Mock CampaignRepository campaignRepository;

  @Mock CompanyService companyService;

  @Mock private ModelMapperBlankString modelMapperBlankString;

  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @Mock private CampaignTopicService campaignTopicService;

  @Mock private PlatformSettingsService platformSettingsService;

  @InjectMocks private CampaignServiceImpl campaignServiceImpl;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
  }

  @Test
  public void findByUrlFriendlyNameOrThrowException_Should_ReturnCampaign_IfUserExists() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    Campaign retVal =
        campaignServiceImpl.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME);

    assertEquals(TEST_CAMPAIGN, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByUrlFriendlyNameOrThrowException_Should_ThrowException_IfUserNotExists() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());

    campaignServiceImpl.findByUrlFriendlyNameOrThrowException(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void PatchCampaign_Should_CallModelMapper() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    CampaignPatchDto campaignPatchDto = CampaignUtils.TEST_CAMPAIGN_PATCH_DTO_FUNDING_GOALS();
    PowerMockito.when(
            campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(testCampaign));
    PowerMockito.when(campaignRepository.save(testCampaign)).thenReturn(testCampaign);
    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Campaign) args[1]).setFundingGoals(CampaignUtils.TEST_FUNDING_GOALS);
              ((Campaign) args[1])
                  .setMinInvestment(PlatformSettingsUtils.TEST_PLATFORM_MINIMUMIM_INVESTMENT);
              return null;
            })
        .when(modelMapperBlankString)
        .map(campaignPatchDto, testCampaign);

    campaignPatchDto.setMinInvestment(new BigDecimal("1000"));
    when(platformSettingsService.getCurrentPlatformSettings())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_SETTINGS_DTO);

    CampaignDto campaignDto =
        campaignServiceImpl.patchCampaign(TEST_URL_FRIENDLY_NAME, campaignPatchDto);
    assertEquals(CampaignUtils.TEST_FUNDING_GOALS, campaignDto.getFundingGoals());
  }

  @Test
  public void CreateCampaign_Should_CreateCampaign() throws Exception {
    when(campaignRepository.findByCompanyIdAndActiveTrueAndDeletedFalse(
            TEST_CAMPAIGN_DTO.getCompanyId()))
        .thenReturn(Optional.empty());
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());
    when(campaignRepository.save(TEST_CAMPAIGN)).thenReturn(TEST_CAMPAIGN);
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());

    TEST_CAMPAIGN_DTO.setMinInvestment(new BigDecimal("1000"));
    when(platformSettingsService.getCurrentPlatformSettings())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_SETTINGS_DTO);

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);

    verify(companyService, Mockito.times(1)).findByIdOrThrowException(anyLong());
    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(campaignRepository, Mockito.times(1)).save(any(Campaign.class));
  }

  @Test(expected = BadRequestException.class)
  public void
      CreateCampaign_Should_ThrowException_When_Min_Investment_Smaller_Than_PlatformMinimum()
          throws Exception {
    when(campaignRepository.findByCompanyIdAndActiveTrueAndDeletedFalse(
            TEST_CAMPAIGN_DTO.getCompanyId()))
        .thenReturn(Optional.empty());
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.empty());
    when(campaignRepository.save(TEST_CAMPAIGN)).thenReturn(TEST_CAMPAIGN);
    when(companyService.findByIdOrThrowException(anyLong()))
        .thenReturn(CompanyUtils.getCompanyMocked());

    TEST_CAMPAIGN_DTO.setMinInvestment(new BigDecimal("400"));
    when(platformSettingsService.getCurrentPlatformSettings())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_SETTINGS_DTO);

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);

    verify(companyService, Mockito.times(1)).findByIdOrThrowException(anyLong());
    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(campaignRepository, Mockito.times(1)).save(any(Campaign.class));
  }

  @Test(expected = ActiveCampaignAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_ActiveCampaignAlreadyExistsException_WhenCampaignNameExists() {
    when(campaignRepository.findByCompanyIdAndActiveTrueAndDeletedFalse(
            TEST_CAMPAIGN_DTO.getCompanyId()))
        .thenReturn(Optional.of(TEST_CAMPAIGN));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }

  @Test(expected = CampaignNameAlreadyExistsException.class)
  public void
      CreateCampaign_Should_Throw_CampaignNameAlreadyExistsException_WhenCampaignNameExists() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(TEST_CAMPAIGN.toBuilder().build()));

    campaignServiceImpl.createCampaign(TEST_CAMPAIGN_DTO);
  }

  @Test
  public void UploadMarketImage_Should_DeleteOldMarketImage_And_SaveToRepository() {
    Campaign campaign = getCampaignMocked();
    campaign.setMarketImageUrl(TEST_FEATURED_IMAGE_URL);
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.uploadMarketImage(TEST_URL_FRIENDLY_NAME, FileUtils.MOCK_FILE_VALID);

    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(
            TEST_FEATURED_IMAGE_URL, campaign.getMarketImageUrl(), FileUtils.MOCK_FILE_VALID);
    verify(campaignRepository, times(1)).save(campaign);
  }

  @Test
  public void GetMarketImage_Should_ReturnMarketImage() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    when(cloudObjectStorageService.downloadFileDto(TEST_MARKET_IMAGE_UTL))
        .thenReturn(FileUtils.TEST_FILE_DTO);

    FileDto returnFileDto = campaignServiceImpl.downloadMarketImage(TEST_URL_FRIENDLY_NAME);

    assertEquals(FileUtils.TEST_FILE_DTO, returnFileDto);
    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(cloudObjectStorageService, Mockito.times(1)).downloadFileDto(TEST_MARKET_IMAGE_UTL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetMarketImage_Should_Throw_EntityNotFoundException() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    doThrow(EntityNotFoundException.class).when(cloudObjectStorageService).downloadFileDto(any());

    campaignServiceImpl.downloadMarketImage(TEST_URL_FRIENDLY_NAME);
  }

  @Test
  public void DeleteMarketImage_Should_DeleteMarketImage() {
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME))
        .thenReturn(Optional.of(getCampaignMocked()));
    doNothing().when(cloudObjectStorageService).delete(TEST_MARKET_IMAGE_UTL);

    campaignServiceImpl.deleteMarketImage(TEST_URL_FRIENDLY_NAME);

    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(TEST_URL_FRIENDLY_NAME);
    verify(cloudObjectStorageService, Mockito.times(1)).delete(TEST_MARKET_IMAGE_UTL);
  }

  @Test
  public void GetActiveCampaignDto_Should_Return_Campaign() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenReturn(getCompanyMocked());
    Campaign campaign = getCampaignMocked();
    when(campaignRepository.findByCompanyIdAndActiveTrueAndDeletedFalse(getCompanyMocked().getId()))
        .thenReturn(Optional.of(campaign));

    campaignServiceImpl.getActiveCampaignDto();

    verify(companyService, Mockito.times(1))
        .findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId());
    verify(campaignTopicService, times(1)).getTopicStatus(campaign);
    verify(campaignRepository, Mockito.times(1))
        .findByCompanyIdAndActiveTrueAndDeletedFalse(getCompanyMocked().getId());
  }

  @Test(expected = EntityNotFoundException.class)
  public void
      GetActiveCampaignForCompany_Should_Throw_EntityNotFoundException_When_No_Active_Campaign() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenReturn(getCompanyMocked());

    campaignServiceImpl.getActiveCampaignForCompany();
  }

  @Test(expected = EntityNotFoundException.class)
  public void
      GetActiveCampaignForCompany_Should_Throw_EntityNotFoundException_When_No_Company_Of_Auth_User() {
    when(companyService.findByAuthIdOrThrowException(TEST_USER_AUTH.getAuth().getId()))
        .thenThrow(new EntityNotFoundException());
    campaignServiceImpl.getActiveCampaignForCompany();
  }

  @Test
  public void DeleteCampaign_Should_SetDeletedFlag() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));

    campaignServiceImpl.delete(testCampaign.getUrlFriendlyName());

    verify(campaignRepository, Mockito.times(1))
        .findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName());
    verify(campaignRepository, Mockito.times(1)).save(testCampaign);
    assertTrue(testCampaign.getDeleted());
  }

  @Test(expected = EntityNotFoundException.class)
  public void DeleteCampaign_Should_Throw_NotFoundException() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.empty());

    campaignServiceImpl.delete(testCampaign.getUrlFriendlyName());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteCampaign_Should_Throw_ForbiddenException() {
    Campaign testCampaign = getCampaignMocked();
    when(campaignRepository.findByUrlFriendlyNameAndDeletedFalse(testCampaign.getUrlFriendlyName()))
        .thenReturn(Optional.of(testCampaign));
    mockSecurityContext(TEST_USER_AUTH2);

    campaignServiceImpl.delete(testCampaign.getUrlFriendlyName());
  }
}
