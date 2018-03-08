package com.example.kalti.diplo.Weather_Models.Forecast;

import java.util.ArrayList;

/**
 * Created by Kalti on 08.03.2018.
 */

public class WeatherForecast {

    public ForecastPlace forecastPlace = new ForecastPlace();
    public String iconData;
    public ForecastTemperature forecastTemperature = new ForecastTemperature();
    public ForecastCondition forecastCondition = new ForecastCondition();
    public ForecastWind forecastWind = new ForecastWind();
    public ForecastSnow forecastSnow = new ForecastSnow();
    public ForecastClouds forecastClouds = new ForecastClouds();


    public ForecastPlace getForecastPlace() {
        return forecastPlace;
    }

    public void setForecastPlace(ForecastPlace forecastPlace) {
        this.forecastPlace = forecastPlace;
    }

    public String getIconData() {
        return iconData;
    }

    public void setIconData(String iconData) {
        this.iconData = iconData;
    }

    public ForecastTemperature getForecastTemperature() {
        return forecastTemperature;
    }

    public void setForecastTemperature(ForecastTemperature forecastTemperature) {
        this.forecastTemperature = forecastTemperature;
    }

    public ForecastCondition getForecastCondition() {
        return forecastCondition;
    }

    public void setForecastCondition(ForecastCondition forecastCondition) {
        this.forecastCondition = forecastCondition;
    }

    public ForecastWind getForecastWind() {
        return forecastWind;
    }

    public void setForecastWind(ForecastWind forecastWind) {
        this.forecastWind = forecastWind;
    }

    public ForecastSnow getForecastSnow() {
        return forecastSnow;
    }

    public void setForecastSnow(ForecastSnow forecastSnow) {
        this.forecastSnow = forecastSnow;
    }

    public ForecastClouds getForecastClouds() {
        return forecastClouds;
    }

    public void setForecastClouds(ForecastClouds forecastClouds) {
        this.forecastClouds = forecastClouds;
    }
}
