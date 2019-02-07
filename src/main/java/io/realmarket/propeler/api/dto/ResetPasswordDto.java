package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("Encapsulates needed for reset password finalization.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
  @NotBlank(message = "Please provide reset password token")
  private String resetPasswordToken;

  @NotBlank(message = "Please provide new password")
  @Size(min = 8, message = "Password must be at least 8 characters long.")
  private String newPassword;
}