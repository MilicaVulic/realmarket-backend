package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApiModel(description = "TotalInvestmentResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalInvestmentsResponseDto {

  private BigDecimal amount;
  private BigDecimal equity;

  public TotalInvestmentsResponseDto(List<Investment> investments) {
    this.amount = BigDecimal.valueOf(0);
    this.equity = BigDecimal.valueOf(0);
    investments.forEach(
        investment -> {
          if (investment.getInvestmentState().getName() == InvestmentStateName.PAID
              || investment.getInvestmentState().getName() == InvestmentStateName.AUDIT_APPROVED) {
            this.amount = this.amount.add(investment.getInvestedAmount());
            this.equity =
                this.equity.add(
                    investment
                        .getInvestedAmount()
                        .multiply(investment.getCampaign().getMinEquityOffered())
                        .divide(
                            BigDecimal.valueOf(investment.getCampaign().getFundingGoals()),
                            4,
                            RoundingMode.DOWN));
          }
        });
  }
}
