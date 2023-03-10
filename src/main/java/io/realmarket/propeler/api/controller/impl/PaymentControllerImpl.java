package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.PaymentController;
import io.realmarket.propeler.api.dto.BankTransferPaymentResponseDto;
import io.realmarket.propeler.api.dto.PayPalPaymentResponseDto;
import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentControllerImpl implements PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentControllerImpl(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @Override
  @GetMapping(value = "/{investmentId}/methods")
  @PreAuthorize("hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR')")
  public ResponseEntity<List<String>> getPaymentMethods(@PathVariable Long investmentId) {
    return ResponseEntity.ok(paymentService.getPaymentMethods(investmentId));
  }

  @Override
  @GetMapping(value = "/{investmentId}/bankTransfer")
  @PreAuthorize("hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR')")
  public ResponseEntity<BankTransferPaymentResponseDto> getBankTransferPayment(
      @PathVariable Long investmentId) {
    return ResponseEntity.ok(
        new BankTransferPaymentResponseDto(paymentService.getBankTransferPayment(investmentId)));
  }

  @Override
  @GetMapping(value = "/{investmentId}/proformaInvoice")
  @PreAuthorize(
      "hasAnyAuthority('ROLE_ADMIN', 'ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR')")
  public ResponseEntity<String> getProformaInvoice(@PathVariable Long investmentId) {
    return ResponseEntity.ok(paymentService.getProformaInvoiceUrl(investmentId));
  }

  @Override
  @PostMapping(value = "/{investmentId}/payPal/{payPalOrderId}")
  @PreAuthorize("hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR')")
  public ResponseEntity<PayPalPaymentResponseDto> confirmPayPalPayment(
      @PathVariable String payPalOrderId, @PathVariable Long investmentId) {
    return ResponseEntity.ok(
        new PayPalPaymentResponseDto(
            paymentService.confirmPayPalPayment(payPalOrderId, investmentId)));
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<PaymentResponseDto>> getPayments(
      Pageable pageable, @RequestParam(value = "filter", required = false) String filter) {
    return ResponseEntity.ok(paymentService.getPayments(pageable, filter));
  }

  @Override
  @PostMapping(value = "/{investmentId}/bankTransfer/confirm")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<PaymentResponseDto> confirmBankTransferPayment(
      @PathVariable Long investmentId,
      @RequestBody @Valid PaymentConfirmationDto paymentConfirmationDto) {
    return ResponseEntity.ok(
        new PaymentResponseDto(
            paymentService.confirmBankTransferPayment(investmentId, paymentConfirmationDto)));
  }

  @Override
  @GetMapping(value = "/{investmentId}/invoice")
  @PreAuthorize(
      "hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_CORPORATE_INVESTOR', 'ROLE_ADMIN')")
  public ResponseEntity<String> getPaymentInvoice(@PathVariable Long investmentId) {
    return ResponseEntity.ok(paymentService.getInvoice(investmentId));
  }
}
