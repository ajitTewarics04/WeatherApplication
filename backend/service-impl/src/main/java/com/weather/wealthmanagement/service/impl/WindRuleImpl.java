package com.weathermanagement.service.impl;

import com.weathermanagement.core.data.DailyForecast;
import com.weathermanagement.core.service.WeatherRule;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WindRuleImpl implements WeatherRule {
    @Override public Optional<String>apply(DailyForecast f) {
        return f.getHigh()>40 ? Optional.of("It’s too windy, watch out!") :Optional.empty();
    }
}
