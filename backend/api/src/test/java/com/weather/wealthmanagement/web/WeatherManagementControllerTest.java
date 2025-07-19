package com.weather.weathermanagement;

import com.weathermanagement.common.data.DailyForecast;
import com.weathermanagement.common.data.response.WeatherResponse;
import com.weathermanagement.ws.api.ForecastService;
import com.weathermanagement.ws.controller.WeatherManagementController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class WeatherManagementControllerTest {

@Mock
ForecastService forecastService;

@InjectMocks
WeatherManagementController controller;

private WeatherResponse request;

@BeforeEach
void setUp() {
request = new WeatherResponse();
}

@Test
void shouldReturnEmptyForecastWhenNoDataFound() {
when(forecastService.getForecast("Pune", LocalDate.now()))
.thenReturn(Optional.empty());

WeatherResponse response = controller.getForecast("Pune");

assertThat(response.getCity()).isEqualTo("Pune");
assertThat(response.getDaily()).isEmpty();
}

@Test
void shouldReturnForecastWhenDataExists() {
LocalDate today = LocalDate.now();
DailyForecast forecast = createDailyForecast(today, 34.5, 24.5);
when(forecastService.getForecast("Delhi", today))
.thenReturn(Optional.of(forecast));

WeatherResponse response = controller.getForecast("Delhi");

assertThat(response.getCity()).isEqualTo("Delhi");
assertThat(response.getDaily()).isNotEmpty();
assertThat(response.getDaily().get(0).getDate()).isEqualTo(today.toString());
}

@Test
void shouldReturnEmptyListWhenServiceReturnsNull() {
when(forecastService.getForecast("Bangalore", LocalDate.now()))
.thenReturn(Optional.empty());

WeatherResponse response = controller.getForecast("Bangalore");

assertThat(response.getDaily()).isEmpty();
}

@Test
void shouldSupportMultipleCities() {
String[] cities = {"Pune", "Mumbai", "Delhi"};
Arrays.stream(cities).forEach(city -> {
when(forecastService.getForecast(eq(city), any()))
.thenReturn(Optional.of(createDailyForecast(LocalDate.now(), 30, 20)));

WeatherResponse response = controller.getForecast(city);

assertThat(response.getCity()).isEqualTo(city);
assertThat(response.getDaily()).isNotEmpty();
});
}

private DailyForecast createDailyForecast(LocalDate date, double high, double low) {
return DailyForecast.builder()
.date(date)
.high(high)
.low(low)
.rainExpected(false)
.build();
}
}