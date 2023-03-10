package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.BankTransferPaymentResponseDto;
import io.realmarket.propeler.api.dto.PayPalPaymentResponseDto;
import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/payments")
public interface PaymentController {

  @ApiOperation(
      value = "Get payment methods",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Payment methods successfully retrieved"),
    @ApiResponse(code = 404, message = "Investment not found.")
  })
  ResponseEntity<List<String>> getPaymentMethods(Long investmentId);

  @ApiOperation(
      value = "Get bank transfer payment",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Bank transfer payment successfully retrieved"),
    @ApiResponse(code = 404, message = "Investment not found.")
  })
  ResponseEntity<BankTransferPaymentResponseDto> getBankTransferPayment(Long investmentId);

  @ApiOperation(
      value = "Verify and capture PayPal payment",
      httpMethod = "POST",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "payPalOrderId",
        value = "PayPal Order ID",
        required = true,
        dataType = "String",
        paramType = "path"),
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "PayPal payment successfully retrieved"),
    @ApiResponse(code = 404, message = "Investment not found.")
  })
  ResponseEntity<PayPalPaymentResponseDto> confirmPayPalPayment(
      String payPalOrderId, Long investmentId);

  @ApiOperation(
      value = "Get proforma invoice for bank transfer payment",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Proforma invoice successfully retrieved"),
    @ApiResponse(code = 404, message = "Investment not found.")
  })
  ResponseEntity<String> getProformaInvoice(Long investmentId);

  @ApiOperation(value = "Get payments", httpMethod = "GET", produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "page",
        value = "Number of page to be returned",
        defaultValue = "20",
        dataType = "Integer",
        paramType = "query"),
    @ApiImplicitParam(
        name = "size",
        value = "Page size (number of items to be returned)",
        defaultValue = "0",
        dataType = "Integer",
        paramType = "query"),
    @ApiImplicitParam(
        name = "filter",
        value = "State of payments to be returned",
        allowableValues = "owner_approved, paid, expired",
        dataType = "String",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Payments successfully retrieved"),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<Page<PaymentResponseDto>> getPayments(Pageable pageable, String filter);

  @ApiOperation(
      value = "Confirm bank transfer payment",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
    @ApiImplicitParam(
        name = "paymentConfirmationDto",
        value = "Dto that contains information about confirmed bank transfer",
        required = true,
        dataType = "PaymentConfirmationDto",
        paramType = "body")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully confirmed bank transfer."),
    @ApiResponse(code = 404, message = "Payment not found.")
  })
  ResponseEntity<PaymentResponseDto> confirmBankTransferPayment(
      Long investmentId, PaymentConfirmationDto paymentConfirmationDto);

  @ApiOperation(
      value = "Get payment invoice",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "investmentId",
        value = "Investment ID",
        required = true,
        dataType = "Long",
        paramType = "path"),
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Payment invoice successfully retrieved"),
    @ApiResponse(code = 404, message = "Investment not found.")
  })
  ResponseEntity<String> getPaymentInvoice(Long investmentId);
}
