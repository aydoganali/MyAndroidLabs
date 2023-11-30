package algonquin.cst2335.aydo0001;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.aydo0001.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView(binding.getRoot());

        binding.getForecast.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName, "UTF-8") + "&appid=b69c61857514fb35f81065ef7b77b61c&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject data = response.getJSONObject("main");
                            binding.temperature.setText("The current temperature is " + data.getString("temp"));
                            binding.maxTemp.setText("The max temperature is " + data.getString("temp_max"));
                            binding.minTemp.setText("The min temperature is " + data.getString("temp_min"));
                            binding.humidity.setText("The humidity is " + data.getString("humidity") + "%");
                            binding.description.setText(weather.getJSONObject(0).getString("description"));
                            String iconName = weather.getJSONObject(0).getString("icon");


                            String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";
                            File f = new File(cityName);
                            if (f.exists()) {
                                binding.weatherImage.setImageBitmap(BitmapFactory.decodeFile(cityName));
                            } else {
                                ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> binding.weatherImage.setImageBitmap(bitmap),
                                        1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                });
                                queue.add(imgReq);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) ->
                    {
                    }
            );

            queue.add(request);


        });
   }
}