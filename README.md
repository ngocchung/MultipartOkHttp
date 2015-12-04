# Multipart OkHttp
Simple Android Application for multipart file uploading using OkHttp (async way).

```
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
```
