package com.weathermanagement.repository;

import com.weathermanagement.core.data.DailyForecast;
import com.weathermanagement.core.repository.ForecastRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class ForecastRepositoryImpl implements ForecastRepository {

    @Value("${openweather.apikey}")
    private String apiKey;

    @Value("${open.weather.api.url}")
    private String openweatherApiUrl;

    private final RestTemplate rt = new RestTemplate();

    private static final double MS_TO_MPH_CONVERSION = 2.237;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int MAX_FORECAST_DAYS = 3;

    private static final Map<String, List<DailyForecast>> FALLBACK = Map.of(
            "testcity", Arrays.asList(
                    new DailyForecast(LocalDate.now().plusDays(1), 45, 30, 5, false, false),
                    new DailyForecast(LocalDate.now().plusDays(2), 25, 15, 12, true, false),
                    new DailyForecast(LocalDate.now().plusDays(3), 28, 18, 8, false, true)
            )
    );

    @Override
    public List<DailyForecast> getNext3Days(String city) {
        try {
            String url = buildApiUrl(city);
            Map<?, ?> response = rt.getForObject(url, Map.class);

            if (response == null) {
log.warn("Received null response from OpenWeather API for city: {}", city);
                return getFallbackForecast(city);
            }

            List<?>forecastList = (List<?>) response.get("list");
            if (forecastList == null || forecastList.isEmpty()) {
log.warn("No forecast data available for city: {}", city);
                return getFallbackForecast(city);
            }

            Map<LocalDate, DailyForecast>dailyForecasts = parseForecastData(forecastList);
            return new ArrayList<>(dailyForecasts.values());

        } catch (Exception ex) {
log.error("Error fetching weather forecast for city: {}", city, ex);
            return getFallbackForecast(city);
        }
    }

    private String buildApiUrl(String city) {
        return String.format(openweatherApiUrl, city, apiKey);
    }

    private Map<LocalDate, DailyForecast>parseForecastData(List<?>forecastList) {
        Map<LocalDate, DailyForecast>dailyForecasts = new HashMap<>();

        for (Object item :forecastList) {
            if (dailyForecasts.size() >= MAX_FORECAST_DAYS) {
break;
            }

            Map<?, ?>forecastItem = (Map<?, ?>) item;
LocalDate date = extractDate(forecastItem);
WeatherDataweatherData = extractWeatherData(forecastItem);

DailyForecastexistingForecast = dailyForecasts.getOrDefault(date,
                    new DailyForecast(date, weatherData.tempMax, weatherData.tempMin,
weatherData.windMph, false, false));

updateDailyForecast(existingForecast, weatherData);
dailyForecasts.put(date, existingForecast);
        }

        return dailyForecasts;
    }

    private LocalDateextractDate(Map<?, ?>forecastItem) {
        long timestamp = ((Number) forecastItem.get("dt")).longValue();
        return LocalDate.ofEpochDay(timestamp / SECONDS_PER_DAY);
    }

    private WeatherDataextractWeatherData(Map<?, ?>forecastItem) {
        Map<?, ?> main = (Map<?, ?>) forecastItem.get("main");
        double tempMax = ((Number) main.get("temp_max")).doubleValue();
        double tempMin = ((Number) main.get("temp_min")).doubleValue();

        Map<?, ?> wind = (Map<?, ?>) forecastItem.get("wind");
        double windSpeedMs = ((Number) wind.get("speed")).doubleValue();
        double windMph = windSpeedMs * MS_TO_MPH_CONVERSION;

        List<?> weather = (List<?>) forecastItem.get("weather");
        Map<?, ?>primaryWeather = (Map<?, ?>) weather.get(0);
        String mainCondition = (String) primaryWeather.get("main");

booleanisRain = "Rain".equalsIgnoreCase(mainCondition);
booleanisThunderstorm = "Thunderstorm".equalsIgnoreCase(mainCondition);

        return new WeatherData(tempMax, tempMin, windMph, isRain, isThunderstorm);
    }

    private void updateDailyForecast(DailyForecast forecast, WeatherDataweatherData) {
forecast.setHigh(Math.max(forecast.getHigh(), weatherData.tempMax));
forecast.setLow(Math.min(forecast.getLow(), weatherData.tempMin));

        if (weatherData.isRain) {
forecast.setRain(true);
        }
        if (weatherData.isThunderstorm) {
forecast.setThunderstorm(true);
        }
    }

    private List<DailyForecast>getFallbackForecast(String city) {
        return FALLBACK.getOrDefault(city.toLowerCase(), Collections.emptyList());
    }

    private static class WeatherData {
        final double tempMax;
        final double tempMin;
        final double windMph;
        final booleanisRain;
        final booleanisThunderstorm;

WeatherData(double tempMax, double tempMin, double windMph, booleanisRain, booleanisThunderstorm) {
this.tempMax = tempMax;
this.tempMin = tempMin;
this.windMph = windMph;
this.isRain = isRain;
this.isThunderstorm = isThunderstorm;
        }
    }
}
