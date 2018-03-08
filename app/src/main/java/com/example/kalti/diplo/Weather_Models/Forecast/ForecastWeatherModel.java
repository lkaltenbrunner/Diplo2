package com.example.kalti.diplo.Weather_Models.Forecast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kalti on 22.02.2018.
 */

public class ForecastWeatherModel {

    ArrayList<WeatherForecast> forecastArrayList = new ArrayList<>();

    public ArrayList<WeatherForecast> getForecastArrayList() {
        return forecastArrayList;
    }

    public void setForecastArrayList(ArrayList<WeatherForecast> forecastArrayList) {
        this.forecastArrayList = forecastArrayList;
    }
}
