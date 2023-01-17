package com.aish_vardhan.memify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    String Curl=null;
    Button next,share;
    ImageView content;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next=findViewById(R.id.next);
        share=findViewById(R.id.share);
        content=findViewById(R.id.meme);
        loadingPB=findViewById(R.id.loadingPB);

        fetch();


    }
    private void fetch(){
        loadingPB.setVisibility(View.VISIBLE);
    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

         String url = "https://meme-api.com/gimme";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            // inside the on response method.
            // we are hiding our progress bar.
            try {
                Curl = response.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Glide.with(MainActivity.this)
                    .load(Curl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadingPB.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loadingPB.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(content);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
        }
        });
    // at last we are adding our json
    // object request to our request
    // queue to fetch all the json data.
        queue.add(jsonObjectRequest);
}
public void nextMeme(View view){
        fetch();
     }

public void shareMeme(View view) {
//    content.setDrawingCacheEnabled(true);
//
//    Bitmap bitmap = content.getDrawingCache();
//    File root = Environment.getExternalStorageDirectory();
//    File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
//    try {
//        cachePath.createNewFile();
//        FileOutputStream ostream = new FileOutputStream(cachePath);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
//        ostream.close();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    BitmapDrawable drawable = (BitmapDrawable) content.getDrawable();
//    Bitmap bitmap = drawable.getBitmap();

    Intent share = new Intent(Intent.ACTION_SEND);
    share.setType("text/plain");
    share.putExtra(Intent.EXTRA_TEXT,"Checkout this meme from reddit \n"+Curl);
    startActivity(Intent.createChooser(share, "Share via"));
}
}
