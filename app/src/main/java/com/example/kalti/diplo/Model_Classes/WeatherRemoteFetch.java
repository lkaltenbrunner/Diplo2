package com.example.kalti.diplo.Model_Classes;

import android.os.AsyncTask;

import com.example.kalti.diplo.WeatherActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kalti on 20.01.2018.
 */

public class WeatherRemoteFetch extends AsyncTask<String, Void, String>{


    @Override
    protected String doInBackground(String... urls) {


        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection)url.openConnection();

            InputStream is = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);

            int data = inputStreamReader.read();

            while(data != -1){
                char current = (char) data;
                result += current;
                data = inputStreamReader.read();

            }

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);


        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = new JSONObject(jsonObject.getString("main"));
            double temperature = Double.parseDouble(weatherData.getString("temp"));
            int tempToInt = (int) ((int)temperature-273.15);

            String placeName = jsonObject.getString("name");
            WeatherActivity.placeTextView.setText(placeName.toString());
            WeatherActivity.tempTextView.setText(Integer.toString(tempToInt));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
