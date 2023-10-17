package algonquin.cst2335.aydo0001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        TextView WelcomeBack = findViewById(R.id.welcomeText);

        String emailAddress = fromPrevious.getStringExtra("EmailAddress"); //if not found, give null
        WelcomeBack.setText("Welcome back " + emailAddress);

        EditText phoneEditText = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("phoneNumber", " ");
        phoneEditText.setText(savedPhoneNumber);

        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener (view -> {
            String phoneNumber = phoneEditText.getText().toString();
            Intent call = new Intent (Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel: " + phoneNumber));
            startActivity(call);
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        ImageView profileIMage = findViewById(R.id.imageView);
                        Intent data = result.getData();
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        if (thumbnail != null) {
                            profileIMage.setImageBitmap(thumbnail);

                            try {
                                FileOutputStream fOut =  openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        Button changePhoto = findViewById(R.id.changePicture);
        changePhoto.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });
        File file = new File (getFilesDir(), "Picture.png");
        if (file.exists())
        {
            String filePath = file.getAbsolutePath();
            Bitmap profileImage = BitmapFactory.decodeFile(filePath);
            ImageView profileImageView = findViewById(R.id.imageView);
            profileImageView.setImageBitmap(profileImage);
        }
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent data = result.getData();
//                            Bitmap thumbnail = data.getParcelableExtra("data");
//                        }
//                    }
//                });



    }
    @Override
    protected void onPause() {
        super.onPause();
        EditText phoneEditText = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("phoneNumber", " ");
        String phoneNumber = phoneEditText.getText().toString();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();

    }}