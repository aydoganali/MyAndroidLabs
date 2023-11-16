package algonquin.cst2335.aydo0001;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import algonquin.cst2335.aydo0001.databinding.ActivityMainBinding;

/**
 * This class is the starting point of the application
 * @author Ali Aydogan
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState A Bundle containing the saved state, if any.
     */
    protected String cityName;
    RequestQueue queue = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getForecast.setOnClickListener(click -> {
            cityName = binding.passwordText.getText().toString();
            String stringURL = "";

            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                    },
                    (error) -> {
                    });
            queue.add(request);

        });
    }







        binding.loginButton.setOnClickListener(click -> {

            String userInput = binding.passwordText.getText().toString();

            if (checkPasswordComplexity(userInput)) {
                binding.textView2.setText("Your password is complex enough");
            }
            else binding.textView2.setText("Your password needs upper and lower case");
        });




    }

    /** This function checks the complexity of the password string
     *
     * @param str The input string to be checked for password complexity.
     * @return Returns true if str has upper and lower case,
     * otherwise false
     */
    boolean checkPasswordComplexity (String str ) {

        boolean foundUpperCase = false;
        boolean foundLowerCase = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(Character.isLowerCase(c))
                foundLowerCase = true;
            else if (Character.isUpperCase(c))
                foundUpperCase = true;


        }
        return foundLowerCase && foundUpperCase;
    }
}