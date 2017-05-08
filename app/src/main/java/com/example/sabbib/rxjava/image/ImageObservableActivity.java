package com.example.sabbib.rxjava.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.sabbib.rxjava.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_PICTURES;

public class ImageObservableActivity extends AppCompatActivity {


    final private String TAG = this.getClass().getSimpleName() + " LOGGING";
    String url = "http://cdn.wallpapersafari.com/82/41/LamTJp.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_observable);

        final ImageView iv1 = (ImageView) findViewById(R.id.imageView);

      getBitmapObservable()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribe(new Subscriber<Bitmap>(){
                       @Override
                    public void onCompleted(){

                    }

                    @Override
                    public void onError(Throwable e){

                    }

                    @Override
                    public void onNext(Bitmap imageBitmap){
                        iv1.setImageBitmap(imageBitmap);
                        saveImageFromBitmap(imageBitmap);
                        Log.d(TAG, "image set");
                    }

                });

    }

    private void saveImageFromBitmap(Bitmap imageBitmap) {
        File imageFile = getOutputPath();
        if (imageFile == null) {
            Log.e(TAG, "Error saving file, Check permissions");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "File not found, check permission " + fnfe.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getOutputPath(){
        File storageDir = new File(getApplicationContext().getExternalFilesDirs(DIRECTORY_PICTURES).toString());

        if(!storageDir.exists()) if(!storageDir.mkdirs()) return null;

        String imageFileName = url.substring(url.lastIndexOf('/')  +  1).split("\\?")[0].split("#")[0];
        File image = new File(storageDir.getPath() + File.separator + imageFileName);
        return image;
    }


    @Nullable
    public InputStream getImageStream() throws IOException{
        try {
            InputStream is = new java.net.URL(url).openStream();
            return is;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public Bitmap getBitmapFromInputStream(InputStream imageStream) throws IOException{
        Bitmap image = BitmapFactory.decodeStream(imageStream);
        imageStream.close();
        return image;
    }

    public Observable<Bitmap> getBitmapObservable(){
        return Observable.defer(new Func0<Observable<Bitmap>>() {

            @Override
            public Observable<Bitmap> call(){
                try{
                    return Observable.just(getBitmapFromInputStream(getImageStream()));
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    return null;
                }
            }

        });
    }
}
