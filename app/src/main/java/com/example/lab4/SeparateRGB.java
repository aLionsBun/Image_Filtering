package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class SeparateRGB extends AppCompatActivity {

    ImageView source, result_red, result_green, result_blue;
    Bitmap source_bitmap;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_rgb);

        source = findViewById(R.id.separate_source);
        result_red = findViewById(R.id.separate_result_red);
        result_green = findViewById(R.id.separate_result_green);
        result_blue = findViewById(R.id.separate_result_blue);
        source_bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.lake);

        source.setImageBitmap(source_bitmap);
        result_red.setImageBitmap(separate_channel(source_bitmap, 0));
        result_green.setImageBitmap(separate_channel(source_bitmap, 1));
        result_blue.setImageBitmap(separate_channel(source_bitmap, 2));

        back = findViewById(R.id.back);
        back.setOnClickListener(e -> finish() );
    }

    public Bitmap separate_channel(Bitmap src, int channel) {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap changed_bitmap = Bitmap.createBitmap(width, height, src.getConfig());

            //Variables with color information of single pixel
        int pixel_color;

            //Scan through every pixel of image
        for(int curr_y=0; curr_y<height; curr_y++)
        {
            for(int curr_x=0; curr_x<width; curr_x++)
            {
                pixel_color = src.getPixel(curr_x, curr_y);
                switch(channel) {
                        //Changing red channel
                    case 0:
                        changed_bitmap.setPixel(curr_x, curr_y,
                                Color.argb(Color.alpha(pixel_color),
                                        Color.red(pixel_color), 0, 0));
                        break;

                        //Changing green channel
                    case 1:
                        changed_bitmap.setPixel(curr_x, curr_y,
                                Color.argb(Color.alpha(pixel_color), 0,
                                        Color.green(pixel_color), 0));
                        break;

                        //Changing blue channel
                    case 2:
                        changed_bitmap.setPixel(curr_x, curr_y,
                                Color.argb(Color.alpha(pixel_color), 0, 0,
                                        Color.blue(pixel_color)));
                        break;
                }
            }
        }
        return changed_bitmap;
    }
}