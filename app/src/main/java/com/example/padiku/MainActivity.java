package com.example.padiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String City = "Bandung";
    String Key = "f7529d49051c5e42ed4f4b5409c54cd1";

    String url = "https://api.openweathermap.org/data/2.5/weather?q=Bandung&units=metric&appid=f7529d49051c5e42ed4f4b5409c54cd1";

    public  class DownloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            String result = "";

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1){

                    result += (char)data;

                    data = inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    TextView txtCity,txtTime,txtValueHumidity,txtTemp,txtPressure,txtGround;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity = findViewById(R.id.cityname);
        txtPressure = findViewById(R.id.pressurenum);
        txtGround = findViewById(R.id.grndlvl);
        txtTime = findViewById(R.id.timenow);
        txtTemp = findViewById(R.id.temperature);
        txtValueHumidity = findViewById(R.id.humidity);

        DownloadJSON downloadJSON = new DownloadJSON();

        try {


            String result = downloadJSON.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);

            String temp = jsonObject.getJSONObject("main").getString("temp");
            String humidity = jsonObject.getJSONObject("main").getString("humidity");
            String pressure = jsonObject.getJSONObject("main").getString("pressure");
            String groundlevel = jsonObject.getJSONObject("main").getString("grnd_level");
            Long time = jsonObject.getLong("dt");
            String sTime = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH).format(new Date(time*1000));


            txtCity.setText(City);
            txtTime.setText(sTime);
            txtPressure.setText(pressure);
            txtGround.setText(groundlevel);
            txtTemp.setText(temp+"°");
            txtValueHumidity.setText(humidity);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set name selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.detection:
                        startActivity(new Intent(getApplicationContext()
                            ,detection.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.reminder:
                        startActivity(new Intent(getApplicationContext()
                                ,reminder.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}