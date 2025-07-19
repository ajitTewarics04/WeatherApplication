package com.weathermanagement.core.repository;

import com.weathermanagement.core.data.DailyForecast;

import java.util.List;

public interface ForecastRepository {
    List<DailyForecast> getNext3Days(String city);
}
