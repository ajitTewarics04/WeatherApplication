package com.weathermanagement.service.impl;

import com.weathermanagement.core.service.WeatherRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WeatherRuleConfiguration {

    @Bean
    public List<WeatherRule>weatherRules() {
        return List.of(
                new HighTempRuleImpl(),
                new RainRuleImpl(),
                new WindRuleImpl(),
                new ThunderStormRuleImpl()
        );
    }
}
