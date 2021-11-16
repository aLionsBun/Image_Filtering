package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class ChangeRGB extends AppCompatActivity {

    ImageView source, result_red, result_green, result_blue;
    Bitmap source_bitmap;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rgb);

        source = findViewById(R.id.change_source);
        result_red = findViewById(R.id.change_result_red);
        result_green = findViewById(R.id.change_result_green);
        result_blue = findViewById(R.id.change_result_blue);
        source_bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ducklings);

        source.setImageBitmap(source_bitmap);
        result_red.setImageBitmap(change_image(source_bitmap, 0));
        result_green.setImageBitmap(change_image(source_bitmap, 1));
        result_blue.setImageBitmap(change_image(source_bitmap, 2));

        back = findViewById(R.id.back);
        back.setOnClickListener(e -> finish() );
    }

    public Bitmap change_image(Bitmap src, int channel) {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap changed_bitmap = Bitmap.createBitmap(width, height, src.getConfig());

            //Variables with color information of single pixel
        int changed_channel, pixel_color;

            //Scan through every pixel of image
        for(int curr_y=0; curr_y<height; curr_y++)
        {
            for(int curr_x=0; curr_x<width; curr_x++)
            {
                pixel_color = src.getPixel(curr_x, curr_y);
                switch(channel) {
                        //Changing red channel
                    case 0:
                        changed_channel = Color.red(pixel_color) % 25;
                        changed_bitmap.setPixel(curr_x, curr_y,
                                Color.argb(Color.alpha(pixel_color), changed_channel,
                                    Color.green(pixel_color), Color.blue(pixel_color)));
                        break;

                        //Changing green channel
                    case 1:
                        changed_channel = Color.green(pixel_color) % 25;
                        changed_bitmap.setPixel(curr_x, curr_y,
                                Color.argb(Color.alpha(pixel_color),
                                    Color.red(pixel_color), changed_channel,
                                    Color.blue(pixel_color)));
                        break;

                        //Changing blue channel
                    case 2:
                        changed_channel = Color.blue(pixel_color) % 25;
                        changed_bitmap.setPixel(curr_x, curr_y,
                                Color.argb(Color.alpha(pixel_color),
                                    Color.red(pixel_color), Color.green(pixel_color),
                                    changed_channel));
                        break;
                }
            }
        }
        return changed_bitmap;
    }
}