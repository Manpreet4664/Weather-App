package com.example.weather_app;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.constants.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.helper.Main;
import com.example.helper.Weather;
import com.example.helper.WeatherHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class will be used to create the
 * weather app which will display the weather
 * of a particular city
 */
public class MainActivity extends AppCompatActivity {

    /*The button to find the weather for a given city*/
    private Button findWeather;

    /*This text field will be used to take city as an input from the user*/
    private EditText cityName;

    /*This field will show the city name after the weather of the city is fetched*/
    private TextView outputCity;

    /*This field will show the temperature of the city*/
    private TextView temperatureDetails;

    /*This field will show the minimum temperature of the city*/
    private TextView minTemp;

    /*This field will show the maximum temperature of the city*/
    private TextView maxTemp;

    /* This field represents the condition of the weather*/
    private TextView mainWeather;

    /*This field represents the description of the weather condition*/
    private TextView weatherDescription;

    /*This field shows the humidity percentage of the city*/
    private TextView humidityPercentage;

    /*This field shows the clouds percentage of the city*/
    private TextView cloudsPercentage;

    /*This field represents the label to show that it represents humidity*/
    private TextView humidityLabel;

    /*This field represents the label to show that it represents clouds*/
    private TextView cloudsLabel;

    /*Variable to check whether a field should be visible or not on the screen*/
    private boolean isVisible = false;

    /**
     * @param savedInstanceState
     * This method is called when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViews();

        // Setting the input in the capital letters
        cityName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        // Setting the text hint for the user
        cityName.setHint("Enter alphabets only...");
        // Setting the UI components to be invisible
        setVisibilityOfViews(isVisible);

        findWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
                String city = cityName.getText().toString();
                if(null!=city && !city.isEmpty()){
                    isVisible = true;
                    setVisibilityOfViews(isVisible);
                    RetrieveWeatherData weatherData = new RetrieveWeatherData();
                    weatherData.execute(city);
                }
                else{
                    setVisibilityOfViews(false);
                    Toast.makeText(view.getContext(),"Please enter the city...",Toast.LENGTH_LONG).show();
                }
                hideDeviceKeyboard();
            }
        });
    }

    /**
     * This method will be used
     * to hide the keyboard which appears
     * while searching the city
     */
    private void hideDeviceKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(findWeather.getWindowToken(),0);
    }

    /**
     * This method will be
     * called before checking the weather to clear the
     * UI fields if any values are
     * present already on the screen
     */
    private void clearFields() {
        outputCity.setText("");
        temperatureDetails.setText("");
        minTemp.setText("");
        maxTemp.setText("");
        mainWeather.setText("");
        weatherDescription.setText("");
        humidityPercentage.setText("");
        cloudsPercentage.setText("");
        humidityLabel.setText("");
        cloudsLabel.setText("");
    }

    /**This method will fetch all
     *the view objects which need to be displayed
     *to show the weather
     */
    private void findAllViews(){
        findWeather = findViewById(R.id.findWeather);
        cityName = findViewById(R.id.cityName);
        outputCity = findViewById(R.id.outputCity);
        temperatureDetails = findViewById(R.id.temperatureDetails);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        mainWeather = findViewById(R.id.mainWeather);
        weatherDescription = findViewById(R.id.weatherDescription);
        humidityPercentage = findViewById(R.id.humidityPercentage);
        cloudsPercentage = findViewById(R.id.cloudsPercentage);
        humidityLabel = findViewById(R.id.humidityLabel);
        cloudsLabel = findViewById(R.id.cloudsLabel);
    }

    /**
     * @param isVisible
     * Method to set the fields visibility
     * on the basis of a flag
     */
    private void setVisibilityOfViews(boolean isVisible){
        if(isVisible){
            outputCity.setVisibility(View.VISIBLE);
            temperatureDetails.setVisibility(View.VISIBLE);
            minTemp.setVisibility(View.VISIBLE);
            maxTemp.setVisibility(View.VISIBLE);
            mainWeather.setVisibility(View.VISIBLE);
            weatherDescription.setVisibility(View.VISIBLE);
            humidityPercentage.setVisibility(View.VISIBLE);
            cloudsPercentage.setVisibility(View.VISIBLE);
            humidityLabel.setVisibility(View.VISIBLE);
            cloudsLabel.setVisibility(View.VISIBLE);
        } else{
            outputCity.setVisibility(View.INVISIBLE);
            temperatureDetails.setVisibility(View.INVISIBLE);
            minTemp.setVisibility(View.INVISIBLE);
            maxTemp.setVisibility(View.INVISIBLE);
            mainWeather.setVisibility(View.INVISIBLE);
            weatherDescription.setVisibility(View.INVISIBLE);
            humidityPercentage.setVisibility(View.INVISIBLE);
            cloudsPercentage.setVisibility(View.INVISIBLE);
            humidityLabel.setVisibility(View.INVISIBLE);
            cloudsLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This inner class
     * will be used to fetch the weather
     * data using HTTP connection
     */
    private class RetrieveWeatherData extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String requestURL = createURL(strings[0]);
            StringBuffer strBuffer = new StringBuffer();
            URL url = null;
            HttpURLConnection httpConn = null;
            InputStream inputStream = null;
            try {
                url = new URL(requestURL);
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                inputStream = httpConn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String readLine;
                while ((readLine = br.readLine()) != null) {
                    strBuffer.append(readLine);
                }

            } catch (Exception e) {
                e.getMessage();
            }
            return strBuffer.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Fetching Data", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Parsing JSON", "Parsing Initiated");
            super.onPostExecute(result);
            JSONObject weatherData = null;
            try {
                weatherData = new JSONObject(result);
                Gson gson = new Gson();
                Type fetchObjects = new TypeToken<WeatherHelper>() {}.getType();
                WeatherHelper weatherHelper = gson.fromJson(result, fetchObjects);
                displayData(weatherHelper);
                Log.i("Parsing JSON", "Parsing Success");
            } catch (JSONException jsException) {
                setVisibilityOfViews(false);
                SystemClock.sleep(1000);
                Toast.makeText(MainActivity.this, "Unable to find the city", Toast.LENGTH_LONG).show();
                jsException.getMessage();
            }
        }
    }

    /**
     * @param weatherHelper
     * This method will be used
     * to display the values of the weather
     * data
     */
    private void displayData(WeatherHelper weatherHelper) {
        if(null!=weatherHelper.getName()) {
            outputCity.setText(weatherHelper.getName().toUpperCase());
        }
        if(null!=weatherHelper.getWeather() && weatherHelper.getWeather().size()!=0){
            Weather weather = weatherHelper.getWeather().get(0);
            weatherDescription.setText(weather.getDescription().toUpperCase());
            mainWeather.setText(weather.getMain().toUpperCase());
        }

        if(null!=weatherHelper.getMain()){
            Main main = weatherHelper.getMain();
            String mainTemp = convertTemperatureToCelsius(main.getTemp());
            String maxTemperature = convertTemperatureToCelsius(main.getTemp_max());
            String minTemperature = convertTemperatureToCelsius(main.getTemp_min());
            maxTemp.setText("Max."+maxTemperature +(char) 0x00B0+"C");
            minTemp.setText("Min."+minTemperature +(char) 0x00B0+"C");
            temperatureDetails.setText(mainTemp +(char) 0x00B0+"C" );
            humidityPercentage.setText(String.valueOf(Math.round(main.getHumidity()))+"%");
            humidityLabel.setText("Humidity");
        }

        if(null!=weatherHelper.getClouds()){
            cloudsLabel.setText("Clouds");
            cloudsPercentage.setText(String.valueOf(Math.round(weatherHelper.getClouds().getAll()))+"%");
        }
    }

    /**
     * @param cityName
     * @return
     * This method will return the URL string after
     * appending the API Key
     */
    private String createURL(String cityName) {
        String url="";
        String applicationURL = Constants.applicationURL;
        String weatherAPIKey = Constants.weatherAPIKey;
        url = applicationURL.concat(cityName).concat(weatherAPIKey);
        return url;
    }

    /**
     * @param inputTemp
     * @return
     * This method will convert Kelvin temperature
     * to Degree Celsius temperature
     */
    private String convertTemperatureToCelsius(double inputTemp){
        double outputTemp = inputTemp - 273.15;
        String celsiusTemp = String.valueOf(Math.round(outputTemp));
        return celsiusTemp;
    }
}