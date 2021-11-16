package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class Filter extends AppCompatActivity {
    TextView result_1_info, result_2_info;
    ImageView source, result_1, result_2;
    Bitmap source_bitmap;
    Button blur, sharp, median, dilation, sobel, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        source = findViewById(R.id.filter_source);
        result_1 = findViewById(R.id.filter_result_1);
        result_1_info = findViewById(R.id.filter_result_1_info);
        result_2 = findViewById(R.id.filter_result_2);
        result_2_info = findViewById(R.id.filter_result_2_info);
        source_bitmap = BitmapFactory.decodeResource(getResources(),
            R.drawable.eagle);
        source.setImageBitmap(source_bitmap);

        blur = findViewById(R.id.blur_button);
        blur.setOnClickListener(e -> blur_filter(source_bitmap, 5));

        sharp = findViewById(R.id.sharp_button);
        sharp.setOnClickListener(e -> sharp_filter(source_bitmap, 1));

        median = findViewById(R.id.median_button);
        median.setOnClickListener(e -> median_filter(source_bitmap));

        dilation = findViewById(R.id.dilation_button);
        dilation.setOnClickListener(e -> dilation_filter(source_bitmap));

        sobel = findViewById(R.id.sobel_button);
        sobel.setOnClickListener(e -> sobel_filter(source_bitmap));

        back = findViewById(R.id.back);
        back.setOnClickListener(e -> finish() );
    }

    public void blur_filter(Bitmap src, int blur_radius)
    {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap changed_bitmap_2 = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888), changed_bitmap_1;

                //Here we have two algorithms

            //First algorithm: using convolution matrix
        float[][] kernel = {
            {0.000789f, 0.006581f, 0.013347f, 0.006581f, 0.000789f},
            {0.006581f, 0.054901f, 0.111345f, 0.054901f, 0.006581f},
            {0.013347f, 0.111345f, 0.225821f, 0.111345f, 0.013347f},
            {0.006581f, 0.054901f, 0.111345f, 0.054901f, 0.006581f},
            {0.000789f, 0.006581f, 0.013347f, 0.006581f, 0.000789f},
        };

        changed_bitmap_1 = apply_matrix_filter(src, width, height, kernel);

        result_1.setVisibility(View.VISIBLE);
        result_1_info.setVisibility(View.VISIBLE);
        result_1.setImageBitmap(changed_bitmap_1);

            //Second algorithm: using RenderScript
        RenderScript render_script = RenderScript.create(getBaseContext());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(render_script,
            Element.U8_4(render_script));
        Allocation in_alloc = Allocation.createFromBitmap(render_script, src);
        Allocation out_alloc = Allocation.createFromBitmap(render_script,
            changed_bitmap_2);
        script.setRadius(blur_radius);
        script.setInput(in_alloc);
        script.forEach(out_alloc);
        out_alloc.copyTo(changed_bitmap_2);
        render_script.destroy();

        result_2.setVisibility(View.VISIBLE);
        result_2_info.setVisibility(View.VISIBLE);
        result_2.setImageBitmap(changed_bitmap_2);
    }

    public void sharp_filter(Bitmap src, float sharp_radius)
    {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap changed_bitmap_2 = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888), changed_bitmap_1;

                //Here we have two algorithms

            //First algorithm: using convolution matrix
        float[][] kernel = {
            {-1f, -1f, -1f},
            {-1f, 9f, -1f},
            {-1f, -1f, -1f},
        };

        changed_bitmap_1=apply_matrix_filter(src, width, height, kernel);

        result_1.setVisibility(View.VISIBLE);
        result_1_info.setVisibility(View.VISIBLE);
        result_1.setImageBitmap(changed_bitmap_1);

            //Second algorithm: using RenderScript
        RenderScript render_script = RenderScript.create(getBaseContext());
        ScriptIntrinsicConvolve3x3 script = ScriptIntrinsicConvolve3x3.create(
            render_script, Element.U8_4(render_script));
        Allocation in_alloc = Allocation.createFromBitmap(render_script, src);
        Allocation out_alloc = Allocation.createFromBitmap(render_script,
            changed_bitmap_2);
        script.setInput(in_alloc);
        float[] sharp_coefficients ={ 0, -sharp_radius, 0, -sharp_radius,
            5f*sharp_radius, -sharp_radius, 0, -sharp_radius, 0};
        script.setCoefficients(sharp_coefficients);
        script.forEach(out_alloc);
        out_alloc.copyTo(changed_bitmap_2);
        render_script.destroy();

        result_2.setVisibility(View.VISIBLE);
        result_2_info.setVisibility(View.VISIBLE);
        result_2.setImageBitmap(changed_bitmap_2);
    }

    public void median_filter(Bitmap src)
    {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap changed_bitmap = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888), temp;

                //Here we use only one algorithm

            //Firstly apply convolution matrix
        float[][] kernel = {
            {1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f},
            {1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f},
            {1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f},
        };

        temp = apply_matrix_filter(src, width, height, kernel);

            //Then apply median filter
        int[] pixel=new int[25], red = new int[25], green = new int[25],
            blue =new int[25];
        for(int pixel_x=2; pixel_x<width-2; pixel_x++)
            for(int pixel_y=2; pixel_y<height-2; pixel_y++)
            {
                pixel[0] = temp.getPixel(pixel_x-2, pixel_y-2);
                pixel[1] = temp.getPixel(pixel_x-2, pixel_y-1);
                pixel[2] = temp.getPixel(pixel_x-2, pixel_y);
                pixel[3] = temp.getPixel(pixel_x-2, pixel_y+1);
                pixel[4] = temp.getPixel(pixel_x-2, pixel_y+2);
                pixel[5] = temp.getPixel(pixel_x-1, pixel_y-2);
                pixel[6] = temp.getPixel(pixel_x-1, pixel_y-1);
                pixel[7] = temp.getPixel(pixel_x-1, pixel_y);
                pixel[8] = temp.getPixel(pixel_x-1, pixel_y+1);
                pixel[9] = temp.getPixel(pixel_x-1, pixel_y+2);
                pixel[10] = temp.getPixel(pixel_x, pixel_y-2);
                pixel[11] = temp.getPixel(pixel_x, pixel_y-1);
                pixel[12] = temp.getPixel(pixel_x, pixel_y);
                pixel[13] = temp.getPixel(pixel_x, pixel_y+1);
                pixel[14] = temp.getPixel(pixel_x, pixel_y+2);
                pixel[15] = temp.getPixel(pixel_x+1, pixel_y-2);
                pixel[16] = temp.getPixel(pixel_x+1, pixel_y-1);
                pixel[17] = temp.getPixel(pixel_x+1, pixel_y);
                pixel[18] = temp.getPixel(pixel_x+1, pixel_y+1);
                pixel[19] = temp.getPixel(pixel_x+1, pixel_y+2);
                pixel[20] = temp.getPixel(pixel_x+2, pixel_y-2);
                pixel[21] = temp.getPixel(pixel_x+2, pixel_y-1);
                pixel[22] = temp.getPixel(pixel_x+2, pixel_y);
                pixel[23] = temp.getPixel(pixel_x+2, pixel_y+1);
                pixel[24] = temp.getPixel(pixel_x+2, pixel_y+2);
                for(int index=0; index<25; index++)
                {
                    red[index] = Color.red(pixel[index]);
                    green[index] = Color.green(pixel[index]);
                    blue[index] = Color.blue(pixel[index]);
                }
                Arrays.sort(red);
                Arrays.sort(green);
                Arrays.sort(blue);
                changed_bitmap.setPixel(pixel_x, pixel_y, Color.argb(255,
                    red[13], green[13], blue[13]));
            }

        result_1.setVisibility(View.VISIBLE);
        result_1_info.setVisibility(View.VISIBLE);
        result_1.setImageBitmap(changed_bitmap);
        result_2.setVisibility(View.GONE);
        result_2_info.setVisibility(View.GONE);
    }

    public void dilation_filter(Bitmap src)
    {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

                //Here we use only one algorithm

            //Just apply convolution matrix
        float[][] kernel = {
            {0.0f, 0.0f, 0.0f},
            {1.0f, 0.0f, 1.0f},
            {0.0f, 0.0f, 0.0f},
        };

        Bitmap changed_bitmap = apply_matrix_filter(src, width, height, kernel);

        result_1.setVisibility(View.VISIBLE);
        result_1_info.setVisibility(View.VISIBLE);
        result_1.setImageBitmap(changed_bitmap);
        result_2.setVisibility(View.GONE);
        result_2_info.setVisibility(View.GONE);
    }

    public void sobel_filter(Bitmap src)
    {
            //Get width and height of original bitmap
        int height = src.getHeight(), width = src.getWidth();

            //Create new bitmap with same parameters as original
        Bitmap changed_bitmap = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888);

                //Here we use only one algorithm

            //Just apply convolution matrix
        int max_gradient = -1;
        int[][] edge_colors = new int[width][height];
        int [][] values = new int[3][3];

        for (int pixel_x=1; pixel_x<width-1; pixel_x++)
        {
            for (int pixel_y=1; pixel_y<height-1; pixel_y++)
            {
                    //Previously convert RGB to grayscale
                values[0][0] = rgb_to_grayscale(src.getPixel(pixel_x-1,
                    pixel_y-1));
                values[0][1] = rgb_to_grayscale(src.getPixel(pixel_x-1,
                    pixel_y));
                values[0][2] = rgb_to_grayscale(src.getPixel(pixel_x-1,
                    pixel_y+1));
                values[1][0] = rgb_to_grayscale(src.getPixel(pixel_x,
                    pixel_y-1));
                values[1][1] = rgb_to_grayscale(src.getPixel(pixel_x,
                    pixel_y));
                values[1][2] = rgb_to_grayscale(src.getPixel(pixel_x,
                    pixel_y+1));
                values[2][0] = rgb_to_grayscale(src.getPixel(pixel_x+1,
                    pixel_y-1));
                values[2][1] = rgb_to_grayscale(src.getPixel(pixel_x+1,
                    pixel_y));
                values[2][2] = rgb_to_grayscale(src.getPixel(pixel_x+1,
                    pixel_y+1));

                int gx =  -values[0][0] + values[0][2] + (-2 * values[1][0]) +
                    (2 * values[1][2]) -values[2][0] + values[2][2];

                int gy =  -values[0][0] + (-2 * values[0][1])-values[0][2] +
                    values[2][0] + (2 * values[2][1]) + values[2][2];

                int g = (int) Math.sqrt((gx * gx) + (gy * gy));
                if(max_gradient < g)
                    max_gradient = g;
                edge_colors[pixel_x][pixel_y] = g;
            }
        }

        double scale = 255.0 / max_gradient;
        for (int curr_x = 1; curr_x < width - 1; curr_x++)
        {
            for (int curr_y = 1; curr_y < height - 1; curr_y++)
            {
                int edge_color = edge_colors[curr_x][curr_y];
                edge_color = (int) (edge_color * scale);
                edge_color = 0xff000000 | (edge_color << 16)
                    | (edge_color << 8) | edge_color;
                changed_bitmap.setPixel(curr_x, curr_y, edge_color);
            }
        }

        result_1.setVisibility(View.VISIBLE);
        result_1_info.setVisibility(View.VISIBLE);
        result_1.setImageBitmap(changed_bitmap);
        result_2.setVisibility(View.GONE);
        result_2_info.setVisibility(View.GONE);
    }

    private Bitmap apply_matrix_filter(Bitmap src, int width, int height,
                                       float[][] kernel)
    {
        Bitmap result = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888);
        for(int pixel_x=0; pixel_x<width; pixel_x++)
        {
            for(int pixel_y=0; pixel_y<height; pixel_y++)
            {
                float red=0, green=0, blue=0;
                for(int kernel_y=0; kernel_y<kernel.length; kernel_y++)
                {
                    for(int kernel_x=0; kernel_x<kernel.length; kernel_x++)
                    {
                        int pixel = src.getPixel(
                            check_range(pixel_x+kernel_x-2, width-1),
                            check_range(pixel_y+kernel_y-2, height-1));
                        red += Color.red(pixel) * kernel[kernel_x][kernel_y];
                        green += Color.green(pixel) * kernel[kernel_x][kernel_y];
                        blue += Color.blue(pixel) * kernel[kernel_x][kernel_y];
                    }
                }
                result.setPixel(pixel_x, pixel_y, Color.argb(255,
                    check_range((int)red, 255),
                    check_range((int)green, 255),
                    check_range((int)blue, 255)));
            }
        }
        return result;
    }

    private int check_range(int value, int max)
    {
        value = Math.min(value, max);
        value = Math.max(value, 0);
        return value;
    }

    private int rgb_to_grayscale(int rgb)
    {
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;
        return (int)(0.2126 * red + 0.7152 * green + 0.0722 * blue);
    }
}