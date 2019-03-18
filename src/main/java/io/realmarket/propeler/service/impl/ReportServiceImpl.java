package io.realmarket.propeler.service.impl;

import com.lowagie.text.DocumentException;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.OTPWildcard;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.ReportService;
import io.realmarket.propeler.service.util.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

  private final PdfService pdfService;
  private final OTPService otpService;

  private static final String PDF_TYPE = "pdf";

  private static final String WILDCARDS_OBJECT_NAME = "wildcards";

  @Autowired
  public ReportServiceImpl(PdfService pdfService, OTPService otpService) {
    this.pdfService = pdfService;
    this.otpService = otpService;
  }

  @Override
  public FileDto generateUserWildcardsPdf(final Long authId) throws DocumentException, IOException {
    Map<String, Object> data =
        Collections.singletonMap(
            WILDCARDS_OBJECT_NAME,
            otpService
                .getWildcardsByAuthId(authId)
                .stream()
                .map(OTPWildcard::getWildcard)
                .collect(Collectors.toList()));

    return new FileDto(
        PDF_TYPE, Base64.getEncoder().encodeToString(pdfService.generateUserWildcardsPdf(data)));
  }
}
