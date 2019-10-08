package com.github.appreciated.designer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${developer}")
    Boolean developerMode;

    public Boolean getDeveloperMode() {
        return developerMode;
    }

    public void setDeveloperMode(Boolean developerMode) {
        this.developerMode = developerMode;
    }
}