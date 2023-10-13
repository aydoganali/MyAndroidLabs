package algonquin.cst2335.aydo0001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import algonquin.cst2335.aydo0001.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    @Override //app starts here
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Log.i(TAG, "MainActivity is now created");


        binding.LoginButton.setOnClickListener ( click -> {
            Log.i(TAG, "You logged in");

            String name = binding.nameText.getText().toString();

            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("UserName", name);

            startActivity( nextPage);

        });

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = prefs.edit();

        String userPhoneNumber = binding.nameText.getText().toString();
        myEditor.putString("PhoneNumber", userPhoneNumber);
        myEditor.apply();

//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater()
//        );
//
//        setContentView(binding.getRoot());
//        Log.i(TAG, "MainActivity is now created");
//
//        binding.nextPaeButton.setOnClickListener( click -> {
//            Log.i(TAG, "You clicked the button");
//
//            String name = binding.nameText.getText().toString();
//                                        //where you are, where you wanna go
//            Intent nextPage = new Intent(this, SecondActivity.class );
//
//            nextPage.putExtra("UserName", name);
////            nextPage.putExtra("Age", 123.456);
//            startActivity(nextPage); //load that page
//        });
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