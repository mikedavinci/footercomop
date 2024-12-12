package wi.roger.rogerWI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class QuestionnaireSecurityConfig implements WebMvcConfigurer {

    @Bean
    public QuestionnaireSecurityInterceptor questionnaireSecurityInterceptor() {
        return new QuestionnaireSecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(questionnaireSecurityInterceptor())
                .addPathPatterns("/api/v1/questionnaire/**");
    }
}