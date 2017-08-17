package com.flybits.webapptester.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.flybits.webapptester.R;

public class UrlEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlentry);

        final EditText edtUrl = findViewById(R.id.activity_urlentry_edtURL);
        Button btnLaunch = findViewById(R.id.activity_urlentry_btnLaunch);

        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UrlEntryActivity.this, MainActivity.class);
                intent.putExtra("url", edtUrl.getText().toString());
                startActivity(intent);
            }
        });
    }

}
