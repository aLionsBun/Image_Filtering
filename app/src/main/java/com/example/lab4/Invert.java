package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Invert extends AppCompatActivity {

    ImageView source, result;
    Bitmap source_bitmap;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invert);

        source = findViewById(R.id.invert_source);
        result = findViewById(R.id.invert_result);
        source_bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.hummingbird);

        source.setImageBitmap(source_bitmap);
        result.setImageBitmap(invert_image());

        back = findViewById(R.id.back);
        back.setOnClickListener(e -> finish() );
    }

    public Bitmap invert_image() {
            //Get width and height of original bitmap
        int height = source_bitmap.getHeight(), width = source_bitmap.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap inverted_bitmap = Bitmap.createBitmap(width, height,
                source_bitmap.getConfig());

            //Variables with color information of single pixel
        int alpha, red, green, blue, pixel_color;

            //Scan through every pixel of image
        for(int curr_y=0; curr_y<height; curr_y++)
        {
            for(int curr_x=0; curr_x<width; curr_x++)
            {
                    //Saving alpha and inverting RGB
                pixel_color = source_bitmap.getPixel(curr_x, curr_y);
                alpha = Color.alpha(pixel_color);
                red = 255 - Color.red(pixel_color);
                green = 255 - Color.green(pixel_color);
                blue = 255 - Color.blue(pixel_color);

                    //Set new pixel to output image
                inverted_bitmap.setPixel(curr_x, curr_y,
                        Color.argb(alpha, red, green, blue));
            }
        }
        return inverted_bitmap;
    }
}