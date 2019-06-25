package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.model.enums.CampaignStateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate, Long> {

  @Query(
      value =
          "SELECT cu FROM Campaign_update cu WHERE campaign IN (SELECT ci.campaign FROM Campaign_investment ci WHERE auth = :auth) ORDER BY post_date DESC")
  Page<CampaignUpdate> findCampaignUpdates(@Param("auth") Auth auth, Pageable pageable);

  @Query(
      value =
          "SELECT cu FROM Campaign_update cu WHERE campaign IN (SELECT ci.campaign FROM Campaign_investment ci WHERE auth = :auth AND campaign IN (SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = :state)) ORDER BY post_date DESC")
  Page<CampaignUpdate> findCampaignUpdatesByCampaignState(
      @Param("auth") Auth auth, @Param("state") CampaignStateName campaignState, Pageable pageable);

  Page<CampaignUpdate> findByCampaign(Campaign campaign, Pageable pageable);
}