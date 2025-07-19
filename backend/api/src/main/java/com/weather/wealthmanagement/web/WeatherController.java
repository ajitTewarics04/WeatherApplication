package com.weather.weathermanagement.ws;

import com.weathermanagement.common.data.dailyforecast.*;
import com.weathermanagement.common.data.request.*;
import com.weathermanagement.common.data.response.*;
import com.weathermanagement.common.link.*;
import com.weathermanagement.ws.api.*;
import com.weathermanagement.ws.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

private static final String DATE_FORMAT = "yyyy-MM-dd";

private final ForecastService forecastService;

@GetMapping("/{city}")
public WeatherResponse getForecast(@PathVariable String city) {
String formattedDate = new SimpleDateFormat(DATE_FORMAT).format(new Date());
Optional<DailyForecast> forecast = forecastService.getForecast(city, formattedDate);

WeatherResponse response = new WeatherResponse(city, forecast);
response.setLinks(generateLinks(city));

return response;
}

private WeatherResponse.Daily toDailyResponse(DailyForecast forecast) {
return new WeatherResponse.Daily(
forecast.getDate(),
forecast.getMinTemp(),
forecast.getMaxTemp(),
Optional.ofNullable(forecast.getRain())
);
}

private ResourceLinks generateLinks(String city) {
WeatherLinks weatherLinks = new WeatherLinks();

Link selfLink = new Link();
selfLink.setRel("self");
selfLink.setHref("/api/weather/" + city);

weatherLinks.setLinks(List.of(selfLink));
return weatherLinks;
}
}