package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button invert, change, rgb, merge, filter, watermark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        invert = findViewById(R.id.invert_button);
        change = findViewById(R.id.change_button);
        rgb = findViewById(R.id.rgb_button);
        merge = findViewById(R.id.merge_button);
        filter = findViewById(R.id.filter_button);
        watermark = findViewById(R.id.watermark_button);

        invert.setOnClickListener(e -> {
            Intent in = new Intent();
            in.setClass(MainActivity.this, Invert.class);
            startActivity(in);
        });

        change.setOnClickListener(e -> {
            Intent in = new Intent();
            in.setClass(MainActivity.this, ChangeRGB.class);
            startActivity(in);
        });

        rgb.setOnClickListener(e -> {
            Intent in = new Intent();
            in.setClass(MainActivity.this, SeparateRGB.class);
            startActivity(in);
        });

        merge.setOnClickListener(e -> {
            Intent in = new Intent();
            in.setClass(MainActivity.this, Merge.class);
            startActivity(in);
        });

        filter.setOnClickListener(e -> {
            Intent in = new Intent();
            in.setClass(MainActivity.this, Filter.class);
            startActivity(in);
        });

        watermark.setOnClickListener(e -> {
            Intent in = new Intent();
            in.setClass(MainActivity.this, Watermark.class);
            startActivity(in);
        });
    }
}