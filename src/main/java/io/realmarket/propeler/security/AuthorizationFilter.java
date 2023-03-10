package io.realmarket.propeler.security;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public final class AuthorizationFilter extends GenericFilterBean {

  private static final String AUTH_HEADER_NAME = "Authorization";
  private static final String AUTH_SCHEME = "Bearer";

  private final JWTService jwtService;
  private final AuthService authService;

  public AuthorizationFilter(JWTService jwtService, AuthService authService) {
    this.jwtService = jwtService;
    this.authService = authService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    log.info("Request - {} {}", httpRequest.getMethod(), httpRequest.getRequestURL().toString());

    try {
      final String authHeader = httpRequest.getHeader(AUTH_HEADER_NAME);
      final String jwtToken = extractJwtFromAuthorizationHeader(authHeader);

      log.info("Jwt from request is {}", jwtToken);

      JWT jwt = jwtService.validateJWTOrThrowException(jwtToken);
      Auth auth = authService.findByIdOrThrowException(jwt.getAuth().getId());
      UserAuthentication userAuth = new UserAuthentication(auth, jwt.getValue());
      SecurityContextHolder.getContext().setAuthentication(userAuth);
      jwtService.prolongExpirationTime(jwt);

      chain.doFilter(request, response);
    } catch (Exception ex) {
      logger.error(ExceptionMessages.INVALID_TOKEN_PROVIDED);
      ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  private String extractJwtFromAuthorizationHeader(final String authHeader) {
    final String[] authHeaderParts = authHeader.split(" ");
    return AUTH_SCHEME.equals(authHeaderParts[0]) ? authHeaderParts[1] : null;
  }
}
