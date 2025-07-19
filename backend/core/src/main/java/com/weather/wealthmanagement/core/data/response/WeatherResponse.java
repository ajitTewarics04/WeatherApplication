
package com.weathermanagement.core.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse implements Serializable {
    private String city;
    private List<DailyDto>daily;

    @Getter
    @Setter
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyDto {
        private LocalDatedate;
        private double high;
        private double low;
        private List<String>advice;

    }
}
