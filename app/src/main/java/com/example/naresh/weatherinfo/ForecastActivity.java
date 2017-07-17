package com.example.naresh.weatherinfo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ForecastActivity extends AppCompatActivity {
    ImageView firstImage,secondImage,thirdImage,fourthImage,fifthImage;
    TextView firstText,secondText,thirdText,fourthText,fifthText;
    ProgressBar loadBar;
    private float temp1;
    private String descr1;
    private String image1;
    private float temp2;
    private String descr2;
    private String image2;
    private float temp3;
    private String descr3;
    private String image3;
    private float temp4;
    private String descr4;
    private String image4;
    private float temp5;
    private String descr5;
    private String image5;
    String city;
    private String image1url1;
    private String image1url2;
    private String image1url3;
    private String image1url4;
    private String image1url5;
    private String dt_txt1;
    private String dt_txt2;
    private String dt_txt3;
    private String dt_txt4;
    private String dt_txt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        firstImage = (ImageView)findViewById(R.id.firstImage);
        secondImage = (ImageView)findViewById(R.id.secondImage);
        thirdImage = (ImageView)findViewById(R.id.thirdImage);
        fourthImage = (ImageView)findViewById(R.id.fourthImage);
        fifthImage = (ImageView)findViewById(R.id.fifthImage);
        firstText = (TextView)findViewById(R.id.firstText);
        secondText = (TextView)findViewById(R.id.secondText);
        thirdText = (TextView)findViewById(R.id.thirdText);
        fourthText = (TextView)findViewById(R.id.fourthText);
        fifthText = (TextView)findViewById(R.id.fifthText);
        Intent intent = getIntent();
        city = intent.getStringExtra("CityNameDisp");
        new Forecast().execute("http://api.openweathermap.org/data/2.5/forecast?id=" + city + "&appid=3e05c42f8532af1d54f66ecb30ed908d");

    }

    public class Forecast extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            BufferedReader reader = null;

            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }

                String finalJSON = buffer.toString();
                try{
                    JSONObject parentObject = new JSONObject(finalJSON);

                    if(parentObject.getString("cod").equals("404")){
                        return "Error 404";
                    }
                    JSONArray list = parentObject.getJSONArray("list");


                    JSONObject day1 = list.getJSONObject(0);
                    JSONObject main1 = day1.getJSONObject("main");
                    JSONArray weather1 = day1.getJSONArray("weather");
                    JSONObject weatherObject1 = weather1.getJSONObject(0);


                    JSONObject day2 = list.getJSONObject(8);
                    JSONObject main2 = day2.getJSONObject("main");
                    JSONArray weather2 = day2.getJSONArray("weather");
                    JSONObject weatherObject2 = weather2.getJSONObject(0);


                    JSONObject day3 = list.getJSONObject(16);
                    JSONObject main3 = day3.getJSONObject("main");
                    JSONArray weather3 = day3.getJSONArray("weather");
                    JSONObject weatherObject3 = weather3.getJSONObject(0);

                    JSONObject day4 = list.getJSONObject(24);
                    JSONObject main4 = day4.getJSONObject("main");
                    JSONArray weather4 = day4.getJSONArray("weather");
                    JSONObject weatherObject4 = weather4.getJSONObject(0);

                    JSONObject day5 = list.getJSONObject(32);
                    JSONObject main5 = day5.getJSONObject("main");
                    JSONArray weather5 = day5.getJSONArray("weather");
                    JSONObject weatherObject5 = weather5.getJSONObject(0);

                    temp1 = main1.getInt("temp");
                    temp1 = temp1-273;
                    descr1 = weatherObject1.getString("description");
                    image1 = weatherObject1.getString("icon");
                    image1url1 = "http://openweathermap.org/img/w/" + image1 + ".png";
                    dt_txt1 = day1.getString("dt_txt");

                    temp2 = main2.getInt("temp");
                    temp2 = temp2-273;
                    descr2 = weatherObject2.getString("description");
                    image2 = weatherObject2.getString("icon");
                    image1url2 = "http://openweathermap.org/img/w/" + image2 + ".png";
                    dt_txt2 = day2.getString("dt_txt");

                    temp3 = main3.getInt("temp");
                    temp3 = temp3-273;
                    descr3 = weatherObject3.getString("description");
                    image3 = weatherObject3.getString("icon");
                    image1url3 = "http://openweathermap.org/img/w/" + image3 + ".png";
                    dt_txt3 = day3.getString("dt_txt");

                    temp4 = main4.getInt("temp");
                    temp4 = temp4-273;
                    descr4 = weatherObject4.getString("description");
                    image4 = weatherObject4.getString("icon");
                    image1url4 = "http://openweathermap.org/img/w/" + image4 + ".png";
                    dt_txt4 = day4.getString("dt_txt");

                    temp5 = main5.getInt("temp");
                    temp5 = temp5-273;
                    descr5 = weatherObject5.getString("description");
                    image5 = weatherObject5.getString("icon");
                    image1url5 = "http://openweathermap.org/img/w/" + image5 + ".png";
                    dt_txt5 = day5.getString("dt_txt");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

            firstText.setText(dt_txt1 +"  : \n"  +temp1 + "℃\n " +  descr1 + "\n"  );
            Picasso.with(getApplicationContext()).load(image1url1).into(firstImage);

            secondText.setText(dt_txt2 +"  :  \n" + temp2 + "℃\n " +  descr2 +  "\n"  );
            Picasso.with(getApplicationContext()).load(image1url2).into(secondImage);

            thirdText.setText(dt_txt3 +"  :  \n" +temp3 + "℃\n " +  descr3 + "\n"  );
            Picasso.with(getApplicationContext()).load(image1url3).into(thirdImage);

            fourthText.setText(dt_txt4 +"  :  \n" +temp4 + "℃\n " +descr4 +   "\n"  );
            Picasso.with(getApplicationContext()).load(image1url4).into(fourthImage);

            fifthText.setText(dt_txt5 +"  :  \n" +temp5 + "℃\n " +  descr5 + "\n"  );
            Picasso.with(getApplicationContext()).load(image1url5).into(fifthImage);
        }
    }
}
