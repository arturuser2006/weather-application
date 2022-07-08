package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String city;
    private Button button;
    private EditText editText;
    TextView textView1, textView2, textView3, textView4, textView5;
    String cityRequested, temp, humidity, wind, pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.cityName);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = editText.getText().toString();
                try {
                    makeHttpConnection(city);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void makeHttpConnection(String cityName) throws IOException, JSONException {
        String connectUrl = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=2eb6eca9a543a55eee0195eb53cbe973";
        URL url = new URL(connectUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int response = connection.getResponseCode();
        if (response == 200) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject coord = jsonObject.getJSONObject("coord");
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");

            double t = Double.parseDouble(main.getString("temp"));
            t = convertKelvinToCelsius(t);

            textView1.setText("Город: " + jsonObject.getString("name"));
            textView2.setText("Температура: " + Double.toString(t) + " °C");
            textView3.setText("Давление: " + main.getString("pressure") + " мм рт. ст.");
            textView4.setText("Влажность: " + main.getString("humidity") + "%");
            textView5.setText("Ветер: " + wind.getString("speed") + " м/с");
        }
    }

    public double convertKelvinToCelsius(double value) {
        value = value-273.15;
        value = Math.round(value);
        return value;
    }
}