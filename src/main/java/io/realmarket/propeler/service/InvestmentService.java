package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PortfolioCampaignResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentService {

  List<Investment> findAllByCampaignAndAuth(Campaign campaign, Auth auth);

  Investment invest(BigDecimal amountOfMoney, String campaignUrlFriendlyName);

  void ownerApproveInvestment(Long investmentId);

  void ownerRejectInvestment(Long investmentId);

  void revokeInvestment(Long investmentId);

  void auditorApproveInvestment(Long investmentId);

  void auditorRejectInvestment(Long investmentId);

  Page<PortfolioCampaignResponseDto> getPortfolio(Pageable pageable, String filter);
}
