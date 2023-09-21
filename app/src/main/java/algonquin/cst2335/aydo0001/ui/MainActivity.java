package algonquin.cst2335.aydo0001.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;

import algonquin.cst2335.aydo0001.R;
import algonquin.cst2335.aydo0001.data.MainViewModel;
import algonquin.cst2335.aydo0001.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView( variableBinding.getRoot() );

        variableBinding.theCheckbox.setOnCheckedChangeListener(
                ( btn, onOrOff ) -> {
                    model.onOrOff.postValue(onOrOff);

                } );

        variableBinding.theSwitch.setOnCheckedChangeListener(
                ( btn, onOrOff) -> {
                    model.onOrOff.postValue(onOrOff);
                }
        );

        variableBinding.theRadioButton.setOnCheckedChangeListener(
                ( btn, onOrOff) -> {
                    model.onOrOff.postValue(onOrOff);
                }
        );

        ImageButton imgBtn = variableBinding.theImageBtn;
        imgBtn.setOnClickListener(clk -> {
            int width = imgBtn.getWidth();
            int height = imgBtn.getWidth();

            String toastMessage = "The width = " + width + "and height = " + height;
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

        });



        model.onOrOff.observe(this, newValue -> {
            variableBinding.theCheckbox.setChecked(newValue);
            variableBinding.theSwitch.setChecked(newValue);
            variableBinding.theRadioButton.setChecked(newValue);

            String toastMessage = "The value is now " + newValue;
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

        });

        TextView mytext = variableBinding.textview;
        Button btn = variableBinding.mybutton;
        EditText myedit = variableBinding.myedittext;

        // this will be called when the value changes
        model.editString.observe(this,
                (s) -> {
            mytext.setText(s);
            btn.setText("Your text is now: " + s);
            });

        myedit.setText(model.editString.getValue());
        //onClickListener       //anonymous class (parantez icindeki)
        btn.setOnClickListener(v -> {
            String string = myedit.getText().toString();
            model.editString.postValue(string);
            btn.setText("You clicked the button");
        });

    }
}