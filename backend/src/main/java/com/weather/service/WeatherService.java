package com.weather.service;

import com.weather.model.WeatherPrediction;
import com.weather.util.WeatherUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private static final String API_KEY = System.getenv("WEATHER_API_KEY");
    private static final String URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&cnt=24";

    public List<WeatherPrediction> getWeatherPrediction(String city) {
        try {
            String response = fetchFromAPI(city);
            return WeatherUtils.parseAndEvaluate(response);
        } catch (Exception e) {
            return WeatherUtils.fallbackData();
        }
    }

    private String fetchFromAPI(String city) throws IOException {
        URL url = new URL(String.format(URL, city, API_KEY));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        }
    }
}
