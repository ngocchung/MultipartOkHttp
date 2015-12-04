package com.example.multipartokhttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final Context mContext = this;
    private static final String LOG_TAG = "OkHttp";
    private TextView mTextView;
    private Handler mHandler;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mHandler = new Handler(Looper.getMainLooper());

        // Multipart request, upload file...
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_launcher);
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            final byte[] bitmapdata = stream.toByteArray();

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"ic_launcher.png\""),
                            RequestBody.create(MEDIA_TYPE_PNG, bitmapdata))
                    .build();
            final Request request = new Request.Builder()
                    .url("http://192.168.1.100:20583/fileupload")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    Log.e(LOG_TAG, e.toString());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            mTextView.setText(e.toString());
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String message = response.toString();
                    Log.i(LOG_TAG, message);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            mTextView.setText(message);
                        }
                    });
                }
            });
        }
    }
}
