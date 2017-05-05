package com.example.sabbib.rxjava.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sabbib.rxjava.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ImageObservable extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_observable);


        getBitmapObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Sche)

    }

    public Observable<ImageFile> getBitmapObservabble(){
        return Observable.defer(()->{
            try{
                return Observable.just(getBitmap());
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        });
    }


    public Bitmap getBitmap() throws IOException{
        String url = "http://api.androidhive.info/images/sample.jpg";
        try {
            InputStream is = new java.net.URL(url).openStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        return null;
        }
    }
}
