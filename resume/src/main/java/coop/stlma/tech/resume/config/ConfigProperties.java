package coop.stlma.tech.resume.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("coop.stlma.tech.resume")
@Getter
@Setter
public class ConfigProperties {
    private List<String> allowedOrigins;
}
