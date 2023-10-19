package algonquin.cst2335.aydo0001;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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