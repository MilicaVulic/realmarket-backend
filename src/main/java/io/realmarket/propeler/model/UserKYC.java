package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_kyc")
public class UserKYC {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_KYC_SEQ")
  @SequenceGenerator(name = "USER_KYC_SEQ", sequenceName = "USER_KYC_SEQ", allocationSize = 1)
  private Long id;

  @JoinColumn(name = "auditorId", foreignKey = @ForeignKey(name = "user_kyc_fk_on_auditor"))
  @ManyToOne
  private Auth auditor;

  @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "user_kyc_fk_on_user"))
  @NotNull
  @ManyToOne
  private Auth user;

  @JoinColumn(
      name = "requestStateId",
      foreignKey = @ForeignKey(name = "user_kyc_fk_on_request_state"))
  @NotNull
  @ManyToOne
  private RequestState requestState;

  @Column(length = 10000)
  private String rejectionReason;

  private Instant uploadDate;

  private boolean politicallyExposed;
}
