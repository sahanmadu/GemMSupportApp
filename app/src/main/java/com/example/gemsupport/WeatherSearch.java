package com.example.gemsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_search);

        final EditText editText=findViewById(R.id.txtNewCity);
        Button backButton=findViewById(R.id.btnNewCitySearch);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity= editText.getText().toString();
                Intent intent=new Intent(WeatherSearch.this,Weather.class);
                intent.putExtra("City",newCity);
                startActivity(intent);



                return false;
            }
        });
    }
}