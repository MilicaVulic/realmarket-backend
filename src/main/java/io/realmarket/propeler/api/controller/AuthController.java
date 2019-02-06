package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.ConfirmRegistrationDto;
import io.realmarket.propeler.api.dto.LoginDto;
import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.api.dto.UsernameDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/auth")
public interface AuthController {

  @ApiOperation(
      value = "/register",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "User successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 409, message = "User with the provided username already exists."),
    @ApiResponse(code = 500, message = "A problem with sending email message has occurred.")
  })
  ResponseEntity register(RegistrationDto registerDto);

  @ApiOperation(
      value = "/confirm_registration",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "User successfully activated."),
    @ApiResponse(code = 400, message = "Invalid token provided.")
  })
  ResponseEntity confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto);

  @ApiOperation(
      value = "/reset_password",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Reset password request created."),
    @ApiResponse(code = 400, message = "Username doesn't exist.")
  })
  ResponseEntity resetPassword(UsernameDto usernameDto);

  @ApiOperation(
      value = "",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Created (if success)"),
    @ApiResponse(code = 400, message = "Invalid Request (on failure to login)\n")
  })
  ResponseEntity login(@RequestBody @Valid LoginDto loginDto);
}
