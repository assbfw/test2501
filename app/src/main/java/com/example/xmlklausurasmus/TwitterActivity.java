package com.example.xmlklausurasmus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.xmlklausurasmus.db.DatabaseHelperOpen;
import com.example.xmlklausurasmus.db.User;

public class TwitterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TABLE_TWUSER = "tbl_twitter";

    // 1. Javaelemente anlegen
    EditText inputUsername, inputPassword;
    Button anmeldeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        // 2. Objekte mit dem XML verknüpfen
        inputUsername = findViewById(R.id.editTextUsername);
        inputPassword = findViewById(R.id.editTextPassword);
        anmeldeBTN = findViewById(R.id.tw_login_btn);
        anmeldeBTN.setOnClickListener(this);

        // Werte herausholen (Siehe Mainactivity Zeile 71 und 72)
        if(getIntent() != null) {
            String username = getIntent().getStringExtra("username");
            String pw = getIntent().getStringExtra("pw");

            if(!username.isEmpty() && !pw.isEmpty()) {
                Log.i("XXX", username);
                Log.i("XXX", pw);
            }
        }
    }

    @Override
    public void onClick(View view) {
        String username = inputUsername.getText().toString();
        String pw = inputPassword.getText().toString();

        try (DatabaseHelperOpen dbHelperOpen = new DatabaseHelperOpen(getApplicationContext())){
            dbHelperOpen.createDataBase();
            User foundedUser = dbHelperOpen.getUserByUsername(username, TABLE_TWUSER);

            // Wenn foundedUser == null, username nicht registriert
            if(foundedUser != null) {
                if (foundedUser.getUsername().equals(username) && foundedUser.getPw().equals(pw)) {
                    Intent mainIntent = new Intent(TwitterActivity.this, MainMenuActivity.class);
                    // Werte dem Intent mitgeben
                    mainIntent.putExtra("username", username);
                    mainIntent.putExtra("pw", pw);
                    mainIntent.putExtra("from", "tw");
                    startActivity(mainIntent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.benutzername_oder_passwort_falsch),
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.sie_sind_noch_nicht_registriert), Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}