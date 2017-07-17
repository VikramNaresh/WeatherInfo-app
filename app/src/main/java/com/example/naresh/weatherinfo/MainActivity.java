package com.example.naresh.weatherinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String entercityname;
    GoogleApiClient googleApiClient;
    String CityID;
    TextView CityNameDisp,WeatherDesc;
    Button search,forecast;
    ImageView image;
    ProgressBar loadbar;
    String imgurl;
    private static final String TAG = "MainActivity";

    Handler handler = new Handler();
    void Displayerror(final String Errortext){
        handler.post(new Runnable() {
            @Override
            public void run() {
                forecast.setEnabled(false);
                Toast.makeText(MainActivity.this, Errortext, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CityNameDisp = (TextView)findViewById(R.id.cty);
        WeatherDesc = (TextView) findViewById(R.id.weatherdesc);
        search = (Button) findViewById(R.id.searchbtn);
        forecast = (Button) findViewById(R.id.btnfrcs) ;
        image = (ImageView) findViewById(R.id.image);
        loadbar = (ProgressBar)findViewById(R.id.loadbar);
        loadbar.setVisibility(View.GONE);
        forecast.setVisibility(View.GONE);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                entercityname=place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


}

    public void onclick1(View view) {
        loadbar.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        CityNameDisp.setVisibility(View.GONE);
        WeatherDesc.setVisibility(View.GONE);
        new APItask().execute("http://api.openweathermap.org/data/2.5/weather?q=" + entercityname + "&appid=3e05c42f8532af1d54f66ecb30ed908d");
    }

    public void onclick2(View view) {
        Intent intent = new Intent(MainActivity.this,ForecastActivity.class);
        intent.putExtra("CityNameDisp",CityID);
        startActivity(intent);
    }


public class APItask extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200){
                enableButton();
                connection.connect();
            }

            else{
                ErrorCase();
                Displayerror("Invalid City Name");
                return null;
            }

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while((line=reader.readLine())!=null) {
                buffer.append(line);
            }

            String finalJSON = buffer.toString();
            try{
                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray weather  = parentObject.getJSONArray("weather");
                JSONObject weatherObject = weather.getJSONObject(0);
                JSONObject main = parentObject.getJSONObject("main");

                String CityNameDisp = parentObject.getString("name");
                String description = weatherObject.getString("description");
                String iconId = weatherObject.getString("icon");
                int temp = main.getInt("temp");
                int humidity = main.getInt("humidity");
                int pressure = main.getInt("pressure");
                long  cityidno = parentObject.getInt("id");
                CityID = "" + cityidno;

                temp = temp - 273;
                imgurl = "http://openweathermap.org/img/w/" + iconId + ".png";

                return  "CityID : "+ CityID +"\n"+"Current temp. : " + temp + "\u2103 \n\n" + "Weather :      " + description + "\n"  + "Pressure:     "
                        + pressure + " hpa\n" + "Humidity:     " + humidity  + "%";

            }
              catch (JSONException e) {
                e.printStackTrace();
            }

            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        loadbar.setVisibility(View.GONE);
        CityNameDisp.setText(entercityname.toString());
        WeatherDesc.setVisibility(View.VISIBLE);
        WeatherDesc.setText(result);
        entercityname="";

        if(result!=null) {
            image.setVisibility(View.VISIBLE);
            CityNameDisp.setVisibility(View.VISIBLE);
        }
        Picasso.with(getApplicationContext()).load(imgurl).into(image);
        forecast.setVisibility(View.VISIBLE);

    }
}


    private void ErrorCase() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CityNameDisp.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
            }
        });
    }

    private void enableButton() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                forecast.setEnabled(true);
            }
        });
    }


}

