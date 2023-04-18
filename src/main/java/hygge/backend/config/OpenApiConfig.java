package hygge.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdic.version}") String springdocVersion) {
        Info info = new Info()
                .title("아주좋은팀 API")
                .version(springdocVersion)
                .description("SW캡스톤디자인 아주좋은팀 API 명세서 ");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
