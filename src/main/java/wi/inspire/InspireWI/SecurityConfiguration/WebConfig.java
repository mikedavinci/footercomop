package wi.roger.rogerWI.SecurityConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${webAdminBaseUrl}")
    private String webAdminBaseUrl;

    @Value("${devserver}")
    private boolean devserver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (devserver) {
            registry.addMapping("/**");
        }
        else {
            registry.addMapping("/**")
                    .allowedOrigins(webAdminBaseUrl)
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("Content-Type", "Authorization")
                    .allowCredentials(true);
        }
    }

}