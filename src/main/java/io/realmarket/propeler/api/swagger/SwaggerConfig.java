package io.realmarket.propeler.api.swagger;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.realmarket.propeler.api.controller"))
        .paths(PathSelectors.ant("/api/*"))
        .build()
        .apiInfo(getApiInfo());
  }

  private ApiInfo getApiInfo() {
    return new ApiInfo(
        "propeler-backend REST API",
        "propeler-backend REST API provides backend logic for the Propeler project.",
        "0.1.0-SNAPSHOT",
        "",
        null,
        "",
        "",
        Collections.emptyList());
  }
}