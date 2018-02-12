package com.example.kalti.diplo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kalti.diplo.Model_Classes.JSONWeatherParser;
import com.example.kalti.diplo.Model_Classes.WeatherForecastRequest;
import com.example.kalti.diplo.Model_Classes.WeatherHttpClient;
import com.example.kalti.diplo.Weather_Models.Weather;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {


    private TextView placeName;
    private TextView temp;
    private TextView clouds;
    private TextView pressure;
    private TextView wind;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;
    private ImageView weatherimg;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        placeName = (TextView) findViewById(R.id.placeTextView);
        temp = (TextView) findViewById(R.id.tempTextView);
        clouds = (TextView) findViewById(R.id.cloudTextView);
        pressure = (TextView) findViewById(R.id.pressureTextView);
        wind = (TextView) findViewById(R.id.windTextView);
        humidity = (TextView) findViewById(R.id.humidityTextView);
        sunrise = (TextView) findViewById(R.id.sunriseTextView);
        sunset = (TextView) findViewById(R.id.sunsetTextView);
        weatherimg = (ImageView) findViewById(R.id.weathericon);

        loadWeatherData("Moscow,RU");
        String wdata = weather.currentCondition.getIcon();
        loadImage(wdata);


    }

    public void loadImage(String code){
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.execute(WeatherForecastRequest.ICON_URL+code+".png");

    }

    public void loadWeatherData(String city){

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city +"&appid=f80945d80f217ddd75999027fedada18"+ "&units=metric"});
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... urls) {

            String urlstr = urls[0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(urlstr).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            weatherimg.setImageBitmap(bitmap);

        }

    }

    private class WeatherTask extends AsyncTask<String, Void, Weather>{
        @Override
        protected Weather doInBackground(String... strings) {

            String data = ((new WeatherHttpClient()).getWeatherData(strings[0]));

            weather = JSONWeatherParser.getWeather(data);


            return weather;
        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            long dvsunrise = Long.valueOf(weather.place.getSunrise())*1000;
            Date dfsunrise = new java.util.Date(dvsunrise);
            String sunriseValue = new SimpleDateFormat("hh:mm:a").format(dfsunrise);

            long dvsunset = Long.valueOf(weather.place.getSunset())*1000;
            Date dfsunset = new java.util.Date(dvsunset);
            String sunsetValue = new SimpleDateFormat("hh:mm:a").format(dfsunset);

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());


            placeName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText(tempFormat + "°C");
            clouds.setText("Condition: " + weather.currentCondition.getCondition());
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            sunrise.setText("Sunrise : " + sunriseValue);
            sunset.setText("Sunset: " + sunsetValue);
        }
    }


}
