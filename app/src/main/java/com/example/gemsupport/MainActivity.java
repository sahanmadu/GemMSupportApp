package com.example.gemsupport;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int screen1=5000;

    Animation topSide,bottomSide;
    ImageView image1;
    TextView text,text1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topSide= AnimationUtils.loadAnimation(this,R.anim.topside);
        bottomSide= AnimationUtils.loadAnimation(this,R.anim.bottomside);

        image1=findViewById(R.id.imageView);
        text=findViewById(R.id.textView);
        text1=findViewById(R.id.textView3);


        image1.setAnimation(topSide);
        text.setAnimation(bottomSide);
        text1.setAnimation(bottomSide);

        //to handle the delay process
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoadF.class);
                startActivity(intent);
                finish();
            }
        },screen1);
    }

}

