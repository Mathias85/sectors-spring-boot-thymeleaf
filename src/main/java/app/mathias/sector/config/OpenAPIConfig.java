package app.mathias.sector.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${sector.openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development environment");

        Info info = new Info()
                .title("Sector Selection API")
                .version("1.0")
                .description("This API exposes endpoints to manage user sector selections.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
} 