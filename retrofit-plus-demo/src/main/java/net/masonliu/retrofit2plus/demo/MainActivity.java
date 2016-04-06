package net.masonliu.retrofit2plus.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.masonliu.retrofit2plus.demo.model.GitResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.plus.RetrofitPlusCallBack;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Call<GitResult> call = RestApi.getApiService(this).getUsersByName("Mason");
        call.enqueue(new Callback<GitResult>() {


            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                GitResult gitResult = response.body();
                Log.e("retrofit_plus1", "success:" + response.raw().request().url().toString() + "\n"
                        + gitResult.getTotalCount());
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable t) {
                Log.e("retrofit_plus1", "onFailure"+t.toString());
            }

            @Override
            public void onCallStart() {

            }

            @Override
            public void onCallFinish() {

            }
        });

        RestApi.getApiService(this).getUsersByName2("Mason",new RetrofitPlusCallBack<GitResult>() {

            @Override
            public void onCallStart() {
            }

            @Override
            public void onCallFinish() {
            }

            @Override
            public void onHttpSuccess(Call<GitResult> call, Response<GitResult> response) {
                GitResult gitResult = response.body();
                Log.e("retrofit_plus2", "success:" + response.raw().request().url().toString() + "\n"
                        + gitResult.getTotalCount());
            }

            @Override
            public void onHttpFailure(Call<GitResult> call, Response<GitResult> response) {
                Log.e("retrofit_plus2", "onHttpFailure");
            }

            @Override
            public void onNetFailure(Call<GitResult> call, Throwable t) {
                Log.e("retrofit_plus2", "onNetFailure"+t.toString());
            }

        });

        RestApi.getApiService(this).testHttps("MasonLiu",new Callback<GitResult>() {

            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                GitResult gitResult = response.body();
                Log.e("retrofit_plus3", "https success:" + response.raw().request().url().toString() +  "\n"+
                        gitResult.getTotalCount());
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable t) {
                Log.e("retrofit_plus3", "onFailure");
            }

            @Override
            public void onCallStart() {

            }

            @Override
            public void onCallFinish() {

            }
        });
    }
}
