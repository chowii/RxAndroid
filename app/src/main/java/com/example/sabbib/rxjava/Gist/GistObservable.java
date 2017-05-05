package com.example.sabbib.rxjava.Gist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.sabbib.rxjava.R;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by sabbib on 5/05/2017.
 */

public class GistObservable extends AppCompatActivity{

    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gist);
        ButterKnife.bind(this);
        getObservable();

    }

    public String getObservable(){
        String gistString = "";
        getGistObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Gist>() {

                    @Override
                    public void onCompleted(){

                    }

                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Gist gist){
                        StringBuilder sb = new StringBuilder();
                        for(Map.Entry<String, GistRepo> entry : gist.files.entrySet()){
                            sb.append(entry.getKey());
                            sb.append(" - ");
                            sb.append("length of file ");
                            sb.append(entry.getValue().content.length());
                            sb.append("\n");

                        text.setText(sb.toString());
                        }
                    }

                });

        return gistString;
    }


    @Nullable
    private Gist getGist() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/gists/59488f02db24ebd83450289e0b0f9ff7")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            Gist gist = new Gson().fromJson(response.body().charStream(), Gist.class);
            return gist;
        }

        return null;
    }


    public Observable<Gist> getGistObservable() {
        return Observable.defer(new Func0<Observable<Gist>>() {

            @Override
            public Observable<Gist> call() {
                try {
                    return Observable.just(getGist());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    return null;
                }
            }
        });
    }
}
