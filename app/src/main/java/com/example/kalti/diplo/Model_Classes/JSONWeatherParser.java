package com.example.kalti.diplo.Model_Classes;

import com.example.kalti.diplo.Weather_Models.Place;
import com.example.kalti.diplo.Weather_Models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kalti on 11.02.2018.
 */

public class JSONWeatherParser {
    public static Weather getWeather(String data) {

        Weather weather = new Weather();

        try {
            JSONObject jsonObject = new JSONObject(data);

            //init JSON-Data
            Place place = new Place();

            JSONObject coordObj = WeatherForecastRequest.getObject("coord",jsonObject);
            place.setLon(WeatherForecastRequest.getFloat("lon", coordObj));
            place.setLat(WeatherForecastRequest.getFloat("lat", coordObj));
            weather.place = place;

            JSONObject sysObj = WeatherForecastRequest.getObject("sys",jsonObject);
            place.setCountry(WeatherForecastRequest.getString("country",sysObj));
            place.setLastupdate(WeatherForecastRequest.getInt("dt",jsonObject));
            place.setSunrise(WeatherForecastRequest.getInt("sunrise",sysObj));
            place.setSunset(WeatherForecastRequest.getInt("sunset",sysObj));
            place.setCity(WeatherForecastRequest.getString("name",jsonObject));

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeatherObject = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(WeatherForecastRequest.getInt("id",jsonWeatherObject));
            weather.currentCondition.setDescription(WeatherForecastRequest.getString("description",jsonWeatherObject));
            weather.currentCondition.setCondition(WeatherForecastRequest.getString("main",jsonWeatherObject));
            weather.currentCondition.setIcon(WeatherForecastRequest.getString("icon",jsonWeatherObject));

            JSONObject mainObj = WeatherForecastRequest.getObject("main", jsonObject);
            weather.currentCondition.setHumidity(WeatherForecastRequest.getInt("humidity", mainObj));
            weather.currentCondition.setPressure(WeatherForecastRequest.getInt("pressure", mainObj));
            weather.currentCondition.setMinTemp(WeatherForecastRequest.getFloat("temp_min", mainObj));
            weather.currentCondition.setMaxTemp(WeatherForecastRequest.getFloat("temp_max", mainObj));
            weather.currentCondition.setTemperature(WeatherForecastRequest.getDouble("temp", mainObj));

            JSONObject windObj = WeatherForecastRequest.getObject("wind",jsonObject);
            weather.wind.setSpeed(WeatherForecastRequest.getFloat("speed",windObj));
            weather.wind.setDeg(WeatherForecastRequest.getFloat("deg",windObj));

            JSONObject cloudObj = WeatherForecastRequest.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(WeatherForecastRequest.getInt("all",cloudObj));



            return weather;



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
