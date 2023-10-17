package algonquin.cst2335.aydo0001;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.aydo0001.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    @Override //app starts here
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        EditText emailEditText = findViewById(R.id.nameText);
        emailEditText.setText(emailAddress);
        Log.i(TAG, "MainActivity is now created");

        EditText Email = findViewById(R.id.nameText);
        Button loginButton = binding.loginButton;
        binding.loginButton.setOnClickListener ( click -> {
            Log.i(TAG, "You logged in");

            String email = Email.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName" , email);
            editor.apply();

            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", email);

            startActivity( nextPage);

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //makes it visible on screen
        Log.i("TAG", "MainActivity is now on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //responding to clicks
        Log.i("TAG", "MainActivity is now responding to clicks");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //no longer responding to clicks
        Log.i("TAG", "MainActivity is no longer responding to clicks");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //no longer visible
        Log.i("TAG", "MainActivity is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //the application is no longer in memory
    }
}