package com.example.kalti.diplo.Model_Classes.Forecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kalti on 22.02.2018.
 */

public class WeatherForecastHttpClient {
    public String getWeatherData(String coord){
        HttpURLConnection connection = null;
        String result = "";
        InputStream is = null;
        try {
            //Connection mit openweathermap API- herstellen => forecast 5 Tage
            URL url = new URL(WeatherForecastRequest.BASE_URL + coord + WeatherForecastRequest.API_KEY);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();



            //Auslesen der Daten
            StringBuffer stringBuffer = new StringBuffer();
            is = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(is)));
            String line = "";

            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\r\n");
            }

            is.close();
            connection.disconnect();
            return stringBuffer.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
