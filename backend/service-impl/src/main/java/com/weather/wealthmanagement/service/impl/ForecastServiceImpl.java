package com.weathermanagement.service.impl;

import com.weathermanagement.core.data.DailyForecast;
import com.weathermanagement.core.repository.ForecastRepository;
import com.weathermanagement.core.service.ForecastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepositoryforecastRepository  forecastRepository;
    @Override
    public List<DailyForecast> getNext3Days(String city) {
        return forecastRepository.getNext3Days(city);
    }
}
