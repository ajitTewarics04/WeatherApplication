package com.weathermanagement.core.service;

import com.weathermanagement.core.data.DailyForecast;

import java.util.List;

public interface ForecastService {
    List<DailyForecast> getNext3Days(String city);
}
