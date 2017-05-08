package com.example.sabbib.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sabbib.rxjava.Gist.GistObservableActivity;
import com.example.sabbib.rxjava.image.ImageObservableActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.gistButton)
    public void gistActivity(){ startActivity(new Intent(this, GistObservableActivity.class)); }

    @OnClick(R.id.imageButton)
    public void imageActivity(){ startActivity(new Intent(this, ImageObservableActivity.class)); }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

}
