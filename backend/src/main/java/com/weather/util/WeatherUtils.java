package com.weather.util;

import com.weather.model.WeatherPrediction;

import java.util.ArrayList;
import java.util.List;

public class WeatherUtils {

    public static List<WeatherPrediction> parseAndEvaluate(String json) {
        List<WeatherPrediction> predictions = new ArrayList<>();
        // Simulated logic only
        predictions.add(new WeatherPrediction("2025-07-12", 41.0, 30.0, List.of("Use sunscreen lotion")));
        predictions.add(new WeatherPrediction("2025-07-13", 36.0, 28.0, List.of("Carry umbrella")));
        predictions.add(new WeatherPrediction("2025-07-14", 35.0, 27.0, List.of("It’s too windy, watch out!")));
        return predictions;
    }

    public static List<WeatherPrediction> fallbackData() {
        return List.of(
            new WeatherPrediction("2025-07-12", 36.0, 28.0, List.of("Data not available. Using fallback.")),
            new WeatherPrediction("2025-07-13", 37.0, 29.0, List.of("Data not available. Using fallback.")),
            new WeatherPrediction("2025-07-14", 35.0, 27.0, List.of("Data not available. Using fallback."))
        );
    }
}
