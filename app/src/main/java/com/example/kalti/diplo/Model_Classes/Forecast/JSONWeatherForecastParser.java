package com.example.kalti.diplo.Model_Classes.Forecast;

import com.example.kalti.diplo.Weather_Models.Forecast.ForecastPlace;
import com.example.kalti.diplo.Weather_Models.Forecast.ForecastWeatherModel;
import com.example.kalti.diplo.Weather_Models.Forecast.WeatherForecast;
import com.example.kalti.diplo.Weather_Models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kalti on 22.02.2018.
 */

public class JSONWeatherForecastParser {
    public static ForecastWeatherModel getForecastWeather(String data) {

        WeatherForecast weatherForecast;
        ForecastWeatherModel forecastWeatherModel = new ForecastWeatherModel();
        ArrayList<WeatherForecast> weatherListArray = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(data);

            if(jsonObject!=null) {



                JSONArray jsonListArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonListArray.length(); i++) {
                    weatherForecast = new WeatherForecast();

                    ForecastPlace forecastPlace = new ForecastPlace();
  //region city
                    JSONObject cityObj = WeatherForecastRequest.getObject("city", jsonObject);
                    JSONObject coordObj = WeatherForecastRequest.getObject("coord", cityObj);

                    if(cityObj.isNull("country")||WeatherForecastRequest.getString("country",cityObj).equals("")){
                        forecastPlace.setCountry("");
                    }else{
                        forecastPlace.setCountry(WeatherForecastRequest.getString("country",cityObj));
                    }

                    if (cityObj.isNull("name")||WeatherForecastRequest.getString("name",cityObj).equals("")){
                        forecastPlace.setCity("Unknown");
                    }else{
                        forecastPlace.setCity(WeatherForecastRequest.getString("name",cityObj));
                    }


                    if(coordObj.isNull("lon")){
                        forecastPlace.setLon(0);
                    }else{
                        forecastPlace.setLon(WeatherForecastRequest.getFloat("lon",coordObj));
                    }
                    if(coordObj.isNull("lat")){
                        forecastPlace.setLat(0);
                    }else{
                        forecastPlace.setLat(WeatherForecastRequest.getFloat("lat",coordObj));
                    }

                    weatherForecast.forecastPlace = forecastPlace;
//endregion city

                    JSONObject listObject = jsonListArray.getJSONObject(i);

                    if (listObject.isNull("dt")){
                        forecastPlace.setLastupdate(0);
                    }else{
                        forecastPlace.setLastupdate(WeatherForecastRequest.getInt("dt", listObject));
                    }
  //region main
                    JSONObject mainObj = WeatherForecastRequest.getObject("main", listObject);




                    if (mainObj.isNull("humidity")){
                        weatherForecast.forecastCondition.setHumidity(0);
                    }else{
                        weatherForecast.forecastCondition.setHumidity(WeatherForecastRequest.getInt("humidity", mainObj));
                    }

                    if (mainObj.isNull("pressure")){
                        weatherForecast.forecastCondition.setPressure(0);
                    }else{
                        weatherForecast.forecastCondition.setPressure(WeatherForecastRequest.getInt("pressure", mainObj));
                    }

                    if (mainObj.isNull("temp_min")){
                        weatherForecast.forecastCondition.setMinTemp(0);
                    }else{
                        weatherForecast.forecastCondition.setMinTemp(WeatherForecastRequest.getFloat("temp_min", mainObj));
                    }
                    if (mainObj.isNull("temp_max")){
                        weatherForecast.forecastCondition.setMaxTemp(0);
                    }else{
                        weatherForecast.forecastCondition.setMaxTemp(WeatherForecastRequest.getFloat("temp_max", mainObj));
                    }

                    if (mainObj.isNull("temp")){
                        weatherForecast.forecastCondition.setTemperature(0);
                    }else{
                        weatherForecast.forecastCondition.setTemperature(WeatherForecastRequest.getDouble("temp", mainObj));
                    }


               //endregion main

  //region weather
                    JSONArray weatherArray = listObject.getJSONArray("weather");
                    JSONObject jsonWeatherObject = weatherArray.getJSONObject(0);

                    if (jsonWeatherObject.isNull("id")){
                        weatherForecast.forecastCondition.setWeatherId(0);
                    }else{
                        weatherForecast.forecastCondition.setWeatherId(WeatherForecastRequest.getInt("id", jsonWeatherObject));
                    }

                    if (jsonWeatherObject.isNull("description")||WeatherForecastRequest.getString("description",jsonWeatherObject).equals("")){
                    }else{
                        weatherForecast.forecastCondition.setDescription(WeatherForecastRequest.getString("description", jsonWeatherObject));
                    }

                    if (jsonWeatherObject.isNull("main")||WeatherForecastRequest.getString("main",jsonWeatherObject).equals("")){
                        weatherForecast.forecastCondition.setCondition("");
                    }else{
                        weatherForecast.forecastCondition.setCondition(WeatherForecastRequest.getString("main", jsonWeatherObject));
                    }

                    if (jsonWeatherObject.isNull("icon")||WeatherForecastRequest.getString("icon",jsonWeatherObject).equals("")){
                        weatherForecast.forecastCondition.setIcon("");
                    }else{
                        weatherForecast.forecastCondition.setIcon(WeatherForecastRequest.getString("icon", jsonWeatherObject));
                    }

//endregion weather
  //region wind

                    JSONObject windObj = WeatherForecastRequest.getObject("wind", listObject);
                    if (windObj.isNull("speed")){
                        weatherForecast.forecastWind.setSpeed(0);
                    }else{
                        weatherForecast.forecastWind.setSpeed(WeatherForecastRequest.getFloat("speed", windObj));
                    }

                    if (windObj.isNull("deg")){
                        weatherForecast.forecastWind.setDeg(0);
                    }else{
                        weatherForecast.forecastWind.setDeg(WeatherForecastRequest.getFloat("deg", windObj));
                    }
//endregion wind
  //region clouds
                    JSONObject cloudObj = WeatherForecastRequest.getObject("clouds", listObject);
                    if (cloudObj.isNull("all")){
                        weatherForecast.forecastClouds.setPrecipitation(0);
                    }else{
                        weatherForecast.forecastClouds.setPrecipitation(WeatherForecastRequest.getInt("all", cloudObj));
                    }
                    //endregion clouds
                    weatherListArray.add(weatherForecast);


                }
                forecastWeatherModel.setForecastArrayList(weatherListArray);
                return  forecastWeatherModel;
            }else{

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
