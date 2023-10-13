package algonquin.cst2335.aydo0001;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.aydo0001.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    String TAG = "SecondActivity";
    private ActivitySecondBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent nextPage = getIntent();
        String userName = nextPage.getStringExtra("UserName"); //if not found, give null


        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textView.setText("Welcome back " + userName);


//        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor myEditor = prefs.edit();
//
//        String userPhoneNumber = binding.editTextPhone.getText().toString();
//        myEditor.putString("PhoneNumber", userPhoneNumber);
//        myEditor.apply();
        binding.callButton.setOnClickListener(click -> {

        String phoneNumber = binding.editTextPhone.getText().toString();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(dialIntent);
        });
//        File mySandbox = getFilesDir();
//        try {
//            openFileOutput("MyFile.txt", Context.MODE_PRIVATE);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                        }
                    }
                });

        binding.button2.setOnClickListener(click -> {


            cameraResult.launch(cameraIntent);


        });
    }}