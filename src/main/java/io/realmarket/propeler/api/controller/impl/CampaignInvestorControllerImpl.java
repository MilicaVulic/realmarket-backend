package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignInvestorController;
import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CampaignInvestorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/campaigns")
@Slf4j
public class CampaignInvestorControllerImpl implements CampaignInvestorController {

  private final CampaignInvestorService campaignInvestorService;

  @Autowired
  public CampaignInvestorControllerImpl(CampaignInvestorService campaignInvestorService) {
    this.campaignInvestorService = campaignInvestorService;
  }

  @Override
  @PostMapping("{campaignName}/investors")
  public ResponseEntity<CampaignInvestorDto> createCampaignInvestor(
      @PathVariable String campaignName, @RequestBody CampaignInvestorDto campaignInvestorDto) {
    return new ResponseEntity<>(
        new CampaignInvestorDto(
            campaignInvestorService.createCampaignInvestor(campaignName, campaignInvestorDto)),
        HttpStatus.CREATED);
  }

  @Override
  @PatchMapping("{campaignName}/investors")
  public ResponseEntity<List<CampaignInvestorDto>> patchCampaignInvestorOrder(
      @PathVariable String campaignName, @RequestBody List<Long> investorOrder) {
    return ResponseEntity.ok(
        campaignInvestorService
            .patchCampaignInvestorOrder(campaignName, investorOrder)
            .stream()
            .map(CampaignInvestorDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @GetMapping("{campaignName}/investors")
  public ResponseEntity<List<CampaignInvestorDto>> getCampaignInvestors(
      @PathVariable String campaignName) {
    return ResponseEntity.ok(
        campaignInvestorService
            .getCampaignInvestors(campaignName)
            .stream()
            .map(CampaignInvestorDto::new)
            .collect(Collectors.toList()));
  }

  @Override
  @PatchMapping("{campaignName}/investors/{investorId}")
  public ResponseEntity<CampaignInvestorDto> patchCampaignInvestor(
      @PathVariable String campaignName,
      @PathVariable Long investorId,
      @RequestBody CampaignInvestorDto campaignInvestorDto) {
    return ResponseEntity.ok(
        new CampaignInvestorDto(
            campaignInvestorService.patchCampaignInvestor(
                campaignName, investorId, campaignInvestorDto)));
  }

  @Override
  @DeleteMapping("{campaignName}/investors/{investorId}")
  public ResponseEntity deleteCampaignInvestor(
      @PathVariable String campaignName, @PathVariable Long investorId) {
    campaignInvestorService.deleteCampaignInvestor(campaignName, investorId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{campaignName}/investors/{investorId}/picture")
  public ResponseEntity uploadPicture(
      @PathVariable String campaignName,
      @PathVariable Long investorId,
      @RequestParam("picture") MultipartFile picture) {
    campaignInvestorService.uploadPicture(campaignName, investorId, picture);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{campaignName}/investors/{investorId}/picture")
  public ResponseEntity<FileDto> downloadPicture(
      @PathVariable String campaignName, @PathVariable Long investorId) {
    return ResponseEntity.ok(campaignInvestorService.downloadPicture(campaignName, investorId));
  }

  @DeleteMapping("/{campaignName}/investors/{investorId}/picture")
  public ResponseEntity deletePicture(
      @PathVariable String campaignName, @PathVariable Long investorId) {
    campaignInvestorService.deletePicture(campaignName, investorId);
    return ResponseEntity.noContent().build();
  }
}
