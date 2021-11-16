package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Watermark extends AppCompatActivity {
    ImageView source, result, source_lsb, result_lsb;
    Bitmap source_bitmap, watermark_bitmap, res_bitmap;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermark);

        source = findViewById(R.id.watermark_source);
        result = findViewById(R.id.watermark_result);
        source_lsb = findViewById(R.id.watermark_bitmap_source);
        result_lsb = findViewById(R.id.watermark_bitmap_result);

            //Getting original image
        source_bitmap = BitmapFactory.decodeResource(getResources(),
            R.drawable.ducklings);
        source.setImageBitmap(source_bitmap);

            //Getting watermark image and changing it to black and white
        watermark_bitmap = BitmapFactory.decodeResource(getResources(),
            R.drawable.watermark_smol);
        watermark_bitmap = prepare_watermark(watermark_bitmap);

            //Getting LSB bitmap of original image
        source_lsb.setImageBitmap(get_lsb_bitmap(source_bitmap));

            //Adding watermark to original image
        res_bitmap = add_watermark(source_bitmap, watermark_bitmap);
        result.setImageBitmap(res_bitmap);

            //Getting LSB bitmap of result image
        result_lsb.setImageBitmap(get_lsb_bitmap(res_bitmap));

        back = findViewById(R.id.back);
        back.setOnClickListener(e -> finish() );
    }

    public Bitmap prepare_watermark(Bitmap src)
    {
            //Get width and height of watermark
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as watermark
        Bitmap result_bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

            //Variable with color information of single pixel
        int val_src, val;

            //Scan through every pixel of image
        for (int curr_y = 0; curr_y < height; curr_y++)
        {
            for (int curr_x = 0; curr_x < width; curr_x++)
            {
                val_src = src.getPixel(curr_x, curr_y);
                val = rgb_to_blackwhite(val_src);
                result_bitmap.setPixel(curr_x, curr_y,
                    Color.argb(Color.alpha(val_src), val, val, val));
            }
        }
        return result_bitmap;
    }

        //Get the bitmap with LSB values
    public Bitmap get_lsb_bitmap(Bitmap src)
    {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap lsb_bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

            //Variable with color information of single pixel
        int val_src, val;

            //Scan through every pixel of image
        for (int curr_y = 0; curr_y < height; curr_y++)
        {
            for (int curr_x = 0; curr_x < width; curr_x++)
            {
                val_src = src.getPixel(curr_x, curr_y);
                    //Set black color if LSB=0, set white if LSB=1
                val = val_src%2==0 ? 0 : 255;
                lsb_bitmap.setPixel(curr_x, curr_y,
                        Color.argb(Color.alpha(val_src), val, val, val));
            }
        }
        return lsb_bitmap;
    }

    public Bitmap add_watermark(Bitmap src, Bitmap mrk)
    {
            //Get width and height of original bitmap
        int height_src = src.getHeight(), width_src = src.getWidth(),
            height_mrk = mrk.getHeight(), width_mrk = mrk.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap res = Bitmap.createBitmap(width_src, height_src,
                Bitmap.Config.ARGB_8888);

            //Variable with color information of single pixel
        int val_src, val_mrk, blue_res;

            //Scan through every pixel of image
        for (int curr_y = 0; curr_y < height_src; curr_y++)
        {
            for (int curr_x = 0; curr_x < width_src; curr_x++)
            {
                val_src = src.getPixel(curr_x, curr_y);

                    //Getting current pixel of watermark depending on source size
                int curr_x_mrk, curr_y_mrk;
                curr_x_mrk = curr_x >= width_mrk ? curr_x-width_mrk : curr_x;
                curr_y_mrk = curr_y >= height_mrk ? curr_y-height_mrk : curr_y;

                    //Set 1 if it's black color, set 0 if it's white color
                val_mrk = Color.red(mrk.getPixel(curr_x_mrk, curr_y_mrk))==0
                    ? 0 : 1;
                blue_res = (Color.blue(val_src) & ~1) | val_mrk;
                res.setPixel(curr_x, curr_y,
                    Color.argb(Color.alpha(val_src), Color.red(val_src),
                    Color.green(val_src), blue_res));
            }
        }
        return res;
    }

        //Returns 255 for white color, 0 for black color
    private int rgb_to_blackwhite(int rgb)
    {
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;
        return ((int)(0.2126 * red + 0.7152 * green + 0.0722 * blue) > 128) ? 255 : 0;
    }
}