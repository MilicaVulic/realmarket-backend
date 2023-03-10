package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestForReviewDetails {
  private Long userKYCId;
  private String requestState;
  private boolean politicallyExposed;

  public RequestForReviewDetails(UserKYC userKYC) {
    this.userKYCId = userKYC.getId();
    this.requestState = userKYC.getRequestState().getName().toString();
    this.politicallyExposed = userKYC.isPoliticallyExposed();
  }
}
