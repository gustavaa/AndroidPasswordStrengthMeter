package se.aaro.gustav.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import se.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator;
import se.aaro.gustav.passwordstrengthmeter.PasswordStrengthLevel;
import se.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Meter initialized from XML
        EditText passwordInput = findViewById(R.id.password_input);
        PasswordStrengthMeter meter1 = findViewById(R.id.passwordInputMeter);
        meter1.setEditText(passwordInput);


        // Meter initialized programmatically
        PasswordStrengthMeter meter = new PasswordStrengthMeter(this);
        meter.setEditText(passwordInput);
        meter.setStrengthLevels(new PasswordStrengthLevel[]{
                new PasswordStrengthLevel("Level 0", android.R.color.white),
                new PasswordStrengthLevel("Level 1", android.R.color.holo_red_light),
                new PasswordStrengthLevel("Level 2", android.R.color.holo_orange_light),
                new PasswordStrengthLevel("Level 4", android.R.color.holo_green_light)});


        LinearLayout ll = findViewById(R.id.container);
        ll.addView(meter);

    }
}
