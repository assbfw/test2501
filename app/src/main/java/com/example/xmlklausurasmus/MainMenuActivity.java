package com.example.xmlklausurasmus;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;


public class MainMenuActivity extends AppCompatActivity {

    private TextView userTV;
    private ImageView iconIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userTV = findViewById(R.id.usernameTV);
        iconIV = findViewById(R.id.imageiconIV);

        // Werte herausholen (Siehe Mainactivity Zeile 71 und 72)
        if(getIntent() != null) {
            String username = getIntent().getStringExtra("username");
            String from = getIntent().getStringExtra("from");

            userTV.setText(username);

            if (from != null && from.equals("fb")) {
                iconIV.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.fb));
            }
            else if (from != null &&  from.equals("tw")) {
                iconIV.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tw));
            }

            // TODO hier gehts weiter mit testen, obs klappt
        }

    }

    
}