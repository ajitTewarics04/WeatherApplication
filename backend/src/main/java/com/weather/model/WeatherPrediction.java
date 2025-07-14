package com.weather.model;

import java.util.List;

public class WeatherPrediction {
    private String date;
    private double highTemp;
    private double lowTemp;
    private List<String> alerts;

    public WeatherPrediction() {}

    public WeatherPrediction(String date, double highTemp, double lowTemp, List<String> alerts) {
        this.date = date;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.alerts = alerts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }
}
