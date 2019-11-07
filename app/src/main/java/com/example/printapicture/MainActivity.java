package com.example.printapicture;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.printapicture.R.drawable;
import com.example.printapicture.R.id;
import com.example.printapicture.R.layout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        //image :
        Options options = new Options();
        options.inMutable = true;
        final Bitmap image = BitmapFactory.decodeResource(getResources(), drawable.f8, options); //initialization

        ImageView iv = findViewById(id.picture);    //emplacement of the picture
        iv.setImageBitmap(image);

        final int w = image.getWidth();
        final int h = image.getHeight();

        TextView tv = findViewById(R.id.sizeIMG);
        tv.setText(w + "*" + h);

        final int [] pixels = new int [w * h];    //save
        image.getPixels(pixels, 0, w, 0,0,w,h);

        //histogram
        ImageView hR = findViewById(R.id.histoR);
        ImageView hG = findViewById(R.id.histoG);
        ImageView hB = findViewById(R.id.histoB);


        final Bitmap histoR = Bitmap.createBitmap(255, 100, Bitmap.Config.RGB_565);
        final Bitmap histoG = Bitmap.createBitmap(255, 100, Bitmap.Config.RGB_565);
        final Bitmap histoB = Bitmap.createBitmap(255, 100, Bitmap.Config.RGB_565);

        Histogram.bitmapHistogram(image, histoR, histoG, histoB);

        hR.setImageBitmap(histoR);
        hG.setImageBitmap(histoG);
        hB.setImageBitmap(histoB);

        // main button
        final Button menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, menuBtn);

                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        TextView tv = findViewById(R.id.timeProcess);
                        if (item.getTitle().equals("ORIGINAL")) {
                            tv.setText("");
                            image.setPixels(pixels, 0, w, 0, 0, w, h);
                        }
                        if (item.getTitle().equals("Shade of gray")) {
                            tv.setText("execution time : "+ Effect.toGray(image) + "ms");
                        }
                        if (item.getTitle().equals("Random color")) {
                            tv.setText("execution time : "+ Effect.colorize(image) + "ms");
                        }
                        if (item.getTitle().equals("Just red")) {
                            tv.setText("execution time : "+ Effect.justInRed(image) + "ms");
                        }
                        if (item.getTitle().equals("Contrast")) {
                            tv.setText("execution time : " + Effect.contrast(image) + "ms");
                        }
                        Histogram.bitmapHistogram(image, histoR, histoG, histoB);
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

}
