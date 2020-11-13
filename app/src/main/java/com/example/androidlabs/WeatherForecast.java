package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "WeatherForecast";
    private static final String ASYNC_NAME = "ForecastQuery";
    private ProgressBar progressBar;
    private TextView uv;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView currentTemp;
    private ImageView currWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        Log.i(ACTIVITY_NAME, ": in onCreate()");
        currWeather = findViewById(R.id.currWeather);
        currentTemp = findViewById(R.id.currentTemp);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        uv = findViewById(R.id.uv);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE); // set progress bar to visible
        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private float uvRating;
        private String currentT;
        private String maxT;
        private String minT;
        private String iconName;
        Bitmap image;
        File file = null;

        @Override
        protected String doInBackground(String... args) {

            Log.i(ASYNC_NAME, ": in doInBackground()");
            try {

                // String encoded = URLEncoder.encode(args[0], "UTF-8"); // encode url
                URL url = new URL(args[0]); // create url object of what server to contact

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // open connection
                InputStream response = urlConnection.getInputStream(); // wait for data

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8"); //response is data from the server


                String rs = null;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        Log.i(ASYNC_NAME, ": XmlPullParser...start of document");
                    }
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.i(ASYNC_NAME, ": XmlPullParser...start tag " + xpp.getName());

                        if (xpp.getName().equals("temperature")) {
                            currentT = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            minT = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxT = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        }
                        if (xpp.getName().equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = xpp.next();

                }

                urlConnection.disconnect(); // finished getting current, min , max
                response.close();
                // check if image already exists in file storage
                image = null;
                String imagefile = iconName + ".png";
                if (fileExistence(imagefile)) {
                    Log.i(ASYNC_NAME, "image already saved in storage.");
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(fis);

                    if (fis != null)
                        fis.close();
                } else { // Image does not already exist
                    Log.i(ASYNC_NAME, "image does not exist. Downloading image");
                    String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";

                    URL urlIcon = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) urlIcon.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        Log.i(ASYNC_NAME, "image downloaded. Saving to local storage...");
                        image = BitmapFactory.decodeStream(connection.getInputStream()); // download image
                    }

                    // Save Bitmap object to local storage
                    FileOutputStream outputStream = openFileOutput(imagefile, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    Log.i(ASYNC_NAME, "image saved.");
                    outputStream.flush();
                    outputStream.close();
                    connection.disconnect();
                }

                //get UV rating
                String uvUrl = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
                URL urlUv = new URL(uvUrl);
                HttpURLConnection conn = (HttpURLConnection) urlUv.openConnection();
                InputStream res = conn.getInputStream();
                conn.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(res, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append((line + "\n"));
                }

                String results = sb.toString();
                JSONObject uvReport = new JSONObject(results);

                uvRating = (float) uvReport.getDouble("value");
                publishProgress(100);

            } catch (IOException | XmlPullParserException | JSONException e) {
                e.printStackTrace();
            }


            return "DONE";
        }

        public boolean fileExistence(String fname) {
            file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... args) {
            super.onProgressUpdate(args);
            Log.i(ASYNC_NAME, ": in onProgressUpdate() setting progress bar to visible");
            progressBar.setVisibility(View.VISIBLE);
            Log.i(ASYNC_NAME, ": in onProgressUpdate() setting progress bar to " + args[0]);
            progressBar.setProgress(args[0]);
            Log.i(ASYNC_NAME, "progressbar @ " + progressBar.getProgress());
        }

        @Override
        protected void onPostExecute(String s) {
            currentTemp.setText("Current: " + currentT + "°C");
            minTemp.setText("Min: " + minT + "°C");
            maxTemp.setText("Max: " + maxT + "°C");
            currWeather.setImageBitmap(image);
            uv.setText("UV: " + uvRating);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}