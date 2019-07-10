package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.annotations.Email;
import io.realmarket.propeler.model.FundraisingProposal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(value = "Fundraising proposal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundraisingProposalResponseDto {

  @ApiModelProperty(value = "Fundraising proposal's id")
  private Long id;

  @NotBlank(message = "First name")
  private String firstName;

  @NotBlank(message = "Last name")
  private String lastName;

  @ApiModelProperty(value = "Company's name")
  @NotBlank
  private String companyName;

  @ApiModelProperty(value = "Company's website")
  private String website;

  @Email
  @NotBlank(message = "E-mail address")
  private String email;

  @ApiModelProperty(value = "Person's phone number")
  private String phoneNumber;

  @NotNull
  @ApiModelProperty(value = "Previously raised amount of money")
  private BigDecimal previouslyRaised;

  @ApiModelProperty(value = "Funding goals")
  @NotNull(message = "Funding goals")
  private Long fundingGoals;

  private String proposalState;

  public FundraisingProposalResponseDto(FundraisingProposal fundraisingProposal) {
    this.id = fundraisingProposal.getId();
    this.firstName = fundraisingProposal.getFirstName();
    this.lastName = fundraisingProposal.getLastName();
    this.companyName = fundraisingProposal.getCompanyName();
    this.website = fundraisingProposal.getWebsite();
    this.email = fundraisingProposal.getEmail();
    this.phoneNumber = fundraisingProposal.getPhoneNumber();
    this.previouslyRaised = fundraisingProposal.getPreviouslyRaised();
    this.fundingGoals = fundraisingProposal.getFundingGoals();
    this.proposalState = fundraisingProposal.getRequestState().getName().toString();
  }
}