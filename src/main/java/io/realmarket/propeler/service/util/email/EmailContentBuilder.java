package io.realmarket.propeler.service.util.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailContentBuilder {

  private TemplateEngine templateEngine;

  @Autowired
  public EmailContentBuilder(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public String build(Map<String, Object> data, String templateName) {
    Context context = new Context();
    context.setVariables(data);
    return templateEngine.process(templateName, context);
  }
}
