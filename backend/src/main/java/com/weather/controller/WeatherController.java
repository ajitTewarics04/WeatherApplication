package com.weather.controller;

import com.weather.model.WeatherPrediction;
import com.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public ResponseEntity<List<WeatherPrediction>> getWeather(@PathVariable String city) {
        return ResponseEntity.ok(weatherService.getWeatherPrediction(city));
    }
}
