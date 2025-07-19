package com.weathermanagement.repository;

import com.weathermanagement.core.data.DailyForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForecastRepositoryImplTest {

    @Mock
    private RestTemplaterestTemplate;

    @InjectMocks
    private ForecastRepositoryImplforecastRepository;

    private static final String API_KEY = "test-api-key";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=imperial";
    private static final String TEST_CITY = "TestCity";

    @BeforeEach
    void setUp() {
ReflectionTestUtils.setField(forecastRepository, "apiKey", API_KEY);
ReflectionTestUtils.setField(forecastRepository, "openweatherApiUrl", API_URL);
ReflectionTestUtils.setField(forecastRepository, "rt", restTemplate);
    }

    @Test
    void getNext3Days_SuccessfulApiCall_ReturnsForecasts() {
        // Given
        Map<String, Object>mockResponse = createMockApiResponse();
        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);

when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertFalse(result.isEmpty());
        verify(restTemplate).getForObject(expectedUrl, Map.class);
    }

    @Test
    void getNext3Days_NullResponse_ReturnsFallback() {
        // Given
        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);
when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(null);

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertTrue(result.isEmpty()); // No fallback for TEST_CITY
    }

    @Test
    void getNext3Days_EmptyForecastList_ReturnsFallback() {
        // Given
        Map<String, Object>mockResponse = new HashMap<>();
mockResponse.put("list", Collections.emptyList());

        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);
when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertTrue(result.isEmpty());
    }

    @Test
    void getNext3Days_NullForecastList_ReturnsFallback() {
        // Given
        Map<String, Object>mockResponse = new HashMap<>();
mockResponse.put("list", null);

        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);
when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertTrue(result.isEmpty());
    }

    @Test
    void getNext3Days_RestClientException_ReturnsFallback() {
        // Given
        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);
when(restTemplate.getForObject(expectedUrl, Map.class))
.thenThrow(new RestClientException("API Error"));

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertTrue(result.isEmpty());
    }

    @Test
    void getNext3Days_TestCityFallback_ReturnsExpectedData() {
        // Given
        String testCity = "testcity";
        String expectedUrl = String.format(API_URL, testCity, API_KEY);
when(restTemplate.getForObject(expectedUrl, Map.class))
.thenThrow(new RestClientException("API Error"));

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(testCity);

        // Then
assertNotNull(result);
assertEquals(3, result.size());

DailyForecastfirstDay = result.get(0);
assertEquals(LocalDate.now().plusDays(1), firstDay.getDate());
assertEquals(45, firstDay.getHigh());
assertEquals(30, firstDay.getLow());
assertEquals(5, firstDay.getWindMph());
assertFalse(firstDay.isRain());
assertFalse(firstDay.isThunderstorm());
    }

    @Test
    void getNext3Days_ParsesWeatherDataCorrectly() {
        // Given
        Map<String, Object>mockResponse = createDetailedMockApiResponse();
        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);

when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertEquals(1, result.size());

DailyForecast forecast = result.get(0);
assertEquals(75.0, forecast.getHigh());
assertEquals(65.0, forecast.getLow());
assertEquals(11.185, forecast.getWindMph(), 0.001); // 5 m/s * 2.237
assertTrue(forecast.isRain());
assertFalse(forecast.isThunderstorm());
    }

    @Test
    void getNext3Days_HandlesThunderstormWeather() {
        // Given
        Map<String, Object>mockResponse = createThunderstormMockApiResponse();
        String expectedUrl = String.format(API_URL, TEST_CITY, API_KEY);

when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // When
        List<DailyForecast> result = forecastRepository.getNext3Days(TEST_CITY);

        // Then
assertNotNull(result);
assertEquals(1, result.size());

DailyForecast forecast = result.get(0);
assertTrue(forecast.isThunderstorm());
assertFalse(forecast.isRain());
    }

    private Map<String, Object>createThunderstormMockApiResponse() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>>forecastList = new ArrayList<>();

        // Create forecast item with thunderstorm weather
        Map<String, Object>forecastItem = new HashMap<>();
forecastItem.put("dt", System.currentTimeMillis() / 1000); // Current timestamp

        // Main weather data
        Map<String, Object> main = new HashMap<>();
main.put("temp_max", 80.0);
main.put("temp_min", 70.0);
forecastItem.put("main", main);

        // Wind data
        Map<String, Object> wind = new HashMap<>();
wind.put("speed", 6.0); // m/s
forecastItem.put("wind", wind);

        // Weather condition - Thunderstorm
        Map<String, Object>weatherCondition = new HashMap<>();
weatherCondition.put("main", "Thunderstorm");
weatherCondition.put("description", "thunderstorm with rain");

        List<Map<String, Object>> weather = new ArrayList<>();
weather.add(weatherCondition);
forecastItem.put("weather", weather);

forecastList.add(forecastItem);
response.put("list", forecastList);

        return response;
    }

    private Map<String, Object>createMockApiResponse() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>>forecastList = new ArrayList<>();

        Map<String, Object>forecastItem = new HashMap<>();
forecastItem.put("dt", System.currentTimeMillis() / 1000);

        Map<String, Object> main = new HashMap<>();
main.put("temp_max", 75.0);
main.put("temp_min", 65.0);
forecastItem.put("main", main);

        Map<String, Object> wind = new HashMap<>();
wind.put("speed", 5.0);
forecastItem.put("wind", wind);

        Map<String, Object>weatherCondition = new HashMap<>();
weatherCondition.put("main", "Clear");
        List<Map<String, Object>> weather = new ArrayList<>();
weather.add(weatherCondition);
forecastItem.put("weather", weather);

forecastList.add(forecastItem);
response.put("list", forecastList);

        return response;
    }

    private Map<String, Object>createDetailedMockApiResponse() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>>forecastList = new ArrayList<>();

        Map<String, Object>forecastItem = new HashMap<>();
forecastItem.put("dt", System.currentTimeMillis() / 1000);

        Map<String, Object> main = new HashMap<>();
main.put("temp_max", 75.0);
main.put("temp_min", 65.0);
forecastItem.put("main", main);

        Map<String, Object> wind = new HashMap<>();
wind.put("speed", 5.0); // This will be converted to 11.185 mph
forecastItem.put("wind", wind);

        Map<String, Object>weatherCondition = new HashMap<>();
weatherCondition.put("main", "Rain");
        List<Map<String, Object>> weather = new ArrayList<>();
weather.add(weatherCondition);
forecastItem.put("weather", weather);

forecastList.add(forecastItem);
response.put("list", forecastList);

        return response;
    }

    private Map<String, Object>createSameDayMockApiResponse() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>>forecastList = new ArrayList<>();

        long baseTimestamp = System.currentTimeMillis() / 1000;

        // Create two forecast items for the same day
        for (int i = 0; i< 2; i++) {
            Map<String, Object>forecastItem = new HashMap<>();
forecastItem.put("dt", baseTimestamp + (i * 3600)); // Same day, different hours

            Map<String, Object> main = new HashMap<>();
main.put("temp_max", 75.0 + i * 5); // Different temps to test aggregation
main.put("temp_min", 65.0 - i * 2);
forecastItem.put("main", main);

            Map<String, Object> wind = new HashMap<>();
wind.put("speed", 5.0);
forecastItem.put("wind", wind);

            Map<String, Object>weatherCondition = new HashMap<>();
weatherCondition.put("main", "Clear");
            List<Map<String, Object>> weather = new ArrayList<>();
weather.add(weatherCondition);
forecastItem.put("weather", weather);

forecastList.add(forecastItem);
        }

response.put("list", forecastList);
        return response;
    }
}
