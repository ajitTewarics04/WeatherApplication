package com.weathermanagement.core.service;

import com.weathermanagement.core.data.DailyForecast;

import java.util.Optional;
public interface WeatherRule {
    Optional<String>apply(DailyForecast f);
}
