package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.PlatformSettings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface PlatformSettingsService {
  PlatformSettings getCurrentPlatformSettings();
}
