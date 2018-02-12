package com.example.kalti.diplo.Model_Classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Kalti on 11.02.2018.
 */

public class WeatherHttpClient {
    public String getWeatherData(String place){
        HttpURLConnection connection = null;
        String result = "";
        InputStream is = null;
        try {
            //Connection mit openweathermap API- herstellen
            URL url = new URL(WeatherForecastRequest.BASE_URL + place);
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
