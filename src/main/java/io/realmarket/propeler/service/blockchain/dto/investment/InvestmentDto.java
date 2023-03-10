package io.realmarket.propeler.service.blockchain.dto.investment;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentDto extends AbstractBlockchainDto {
  private InvestmentDetails investment;

  public InvestmentDto(Investment investment) {
    this.userId = investment.getPerson().getId();
    this.investment = new InvestmentDetails(investment);
  }

  public InvestmentDto(Investment investment, Long adminId) {
    this.userId = investment.getPerson().getId();
    this.investment = new InvestmentDetails(investment, adminId);
  }
}
