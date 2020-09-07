package eu.agentsunited.topicselectionengine.controller;

import eu.agentsunited.topicselectionengine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Controller
public class SwaggerController {
	
	@Autowired
	private ServletContext context;

	@RequestMapping("/")
	public RedirectView redirectRoot() {
		return new RedirectView(ServiceContext.getBaseUrl() +
				"/swagger-ui.html");
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.regex(".*"))
			.build()
			.securitySchemes(Collections.singletonList(apiKey()))
			.securityContexts(Collections.singletonList(securityContext()))
			.pathProvider(new PathProvider());
	}
	
	private ApiKey apiKey() {
		return new ApiKey("X-Auth-Token", "X-Auth-Token", "header");
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.any())
				.build();
	}
	
	private List<SecurityReference> defaultAuth() {
		return Collections.singletonList(new SecurityReference(
				"X-Auth-Token", new AuthorizationScope[0]));
	}
	
	private class PathProvider extends RelativePathProvider {
		public PathProvider() {
			super(context);
		}

		@Override
		protected String applicationPath() {
			return ServiceContext.getBasePath();
		}
	}
}
