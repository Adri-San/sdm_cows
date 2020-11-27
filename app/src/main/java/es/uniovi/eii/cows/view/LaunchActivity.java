package es.uniovi.eii.cows.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Animation
        ActivityOptions animation = ActivityOptions.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out);

        // Intent to start MainActivity
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent, animation.toBundle());
        finish(); // don't return to this activity
    }
}
