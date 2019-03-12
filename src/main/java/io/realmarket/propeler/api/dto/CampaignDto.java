package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.UnsignedInteger;
import io.realmarket.propeler.api.annotations.UrlFriendly;
import io.realmarket.propeler.model.Campaign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ApiModel(description = "Dto used for transfer of campaign data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDto {

  @ApiModelProperty(value = "Campaign company id")
  @NotNull(message = "Campaign company id must not be null")
  private Long companyId;

  @ApiModelProperty(value = "Campaign name")
  @UrlFriendly(isSpaceAllowed = true)
  @Size(max = 32, message = "Campaign name cannot be longer than 32 characters.")
  private String name;

  @ApiModelProperty(value = "Url friendly version of campaign name")
  @UrlFriendly
  private String urlFriendlyName;

  @ApiModelProperty(value = "Campaign funding goals")
  @NotNull(message = "Please provide funding goals")
  private Long fundingGoals;

  @ApiModelProperty(value = "Campaign time to raise funds")
  @NotNull(message = "Please provide time to raise funds")
  @Max(value = 90, message = "Please provide time to raise funds that is between 1 and 90 days")
  private UnsignedInteger timeToRaiseFunds;

  @ApiModelProperty(value = "Campaign min equity offered")
  @NotNull(message = "Please provide min equity offered")
  @Digits(
      integer = 3,
      fraction = 2,
      message =
          "Min equity offered must be in range of 0.00 to 100.00 with max of 2 values behind decimal point")
  private BigDecimal minEquityOffered;

  @ApiModelProperty(value = "Campaign max equity offered")
  @NotNull(message = "Please provide max equity offered")
  @Digits(
      integer = 3,
      fraction = 2,
      message =
          "Max equity offered must be in range of 0.00 to 100.00 with max of 2 values behind decimal point")
  private BigDecimal maxEquityOffered;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String marketImageUrl;

  public CampaignDto(Campaign campaign) {
    this.companyId = campaign.getCompany().getId();
    this.name = campaign.getName();
    this.urlFriendlyName = campaign.getUrlFriendlyName();
    this.fundingGoals = campaign.getFundingGoals();
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
    this.minEquityOffered = campaign.getMinEquityOffered();
    this.maxEquityOffered = campaign.getMaxEquityOffered();
    this.marketImageUrl = campaign.getMarketImageUrl();
  }
}