package com.example.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class MvcConfig extends WebMvcConfigurationSupport{

	/*@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {

		super.addResourceHandlers(registry);		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);
	}*/
	
	

}
