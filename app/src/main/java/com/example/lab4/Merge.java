package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Merge extends AppCompatActivity {

    Bitmap source_bitmap_1, source_bitmap_2;
    ImageView source_1, source_2, result;
    SeekBar seeker;
    TextView info;
    Button back;
    double percentage = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);

        source_1 = findViewById(R.id.merge_source_1);
        source_2 = findViewById(R.id.merge_source_2);
        result = findViewById(R.id.merge_result);
        source_bitmap_1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.ducklings);
        source_bitmap_2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.lake);

            //Change percentage of first image in result
        seeker = findViewById(R.id.percentage);
        info = findViewById(R.id.percent_info);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                percentage = (float)i/(float)10;
                info.setText(Integer.toString(i *10));
                result.setImageBitmap(merge_images());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        source_1.setImageBitmap(source_bitmap_1);
        source_2.setImageBitmap(source_bitmap_2);
        result.setImageBitmap(merge_images());

        back = findViewById(R.id.back);
        back.setOnClickListener(e -> finish());
    }

    public Bitmap merge_images() {
            //Get width and height of bigger bitmap
        int height = source_bitmap_1.getHeight(), width = source_bitmap_1.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap merged_bitmap = Bitmap.createBitmap(width, height,
                source_bitmap_1.getConfig());

            //Variables with color information of single pixel
        int pixel_1, pixel_2;
        int alpha_result, red_result, green_result, blue_result;

            //Scan through every pixel of image
        for (int curr_y = 0; curr_y < height; curr_y++) {
            for (int curr_x = 0; curr_x < width; curr_x++) {
                pixel_1 = source_bitmap_1.getPixel(curr_x, curr_y);
                pixel_2 = source_bitmap_2.getPixel(curr_x, curr_y);
                alpha_result = (int)(percentage * Color.alpha(pixel_1)
                        + (1-percentage) * Color.alpha(pixel_2));
                red_result = (int)(percentage * Color.red(pixel_1)
                        + (1-percentage) * Color.red(pixel_2));
                green_result = (int)(percentage * Color.green(pixel_1)
                        + (1-percentage) * Color.green(pixel_2));
                blue_result = (int)(percentage * Color.blue(pixel_1)
                        + (1-percentage) * Color.blue(pixel_2));
                merged_bitmap.setPixel(curr_x, curr_y, Color.argb(alpha_result,
                        red_result, green_result, blue_result));
            }
        }
        return merged_bitmap;
    }
}