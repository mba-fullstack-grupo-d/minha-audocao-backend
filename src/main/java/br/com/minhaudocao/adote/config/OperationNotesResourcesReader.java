package br.com.minhaudocao.adote.config;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.Optional;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class OperationNotesResourcesReader implements OperationBuilderPlugin {

    private final DescriptionResolver descriptions;

    @Autowired
    public OperationNotesResourcesReader(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(OperationContext context) {
        try {

            String apiRoleAccessNoteText = "Roles : Nil";
            Optional<PreAuthorize> preAuthorizeAnnotation = context.findAnnotation(PreAuthorize.class);
            if (preAuthorizeAnnotation.isPresent()) {
                apiRoleAccessNoteText = "Roles : " + preAuthorizeAnnotation.get().value();
            }
            // add the note text to the Swagger UI
            context.operationBuilder().notes(descriptions.resolve(apiRoleAccessNoteText));
        } catch (Exception e) {
            System.out.println("Error when creating swagger documentation for security roles: " + e);
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}