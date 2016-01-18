package net.masonliu.retrofit2plus.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.masonliu.retrofit2plus.demo.model.GitResult;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.plus.RetrofitPlusCallBack;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Call<GitResult> call = RestApi.getApiService().getUsersByName("Mason","ss");
//        call.enqueue(new Callback<GitResult>() {
//
//            @Override
//            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
//                GitResult gitResult = response.body();
//                Log.e("retrofit_plus", "success:" + response.raw().request().url().toString() + "\n"
//                        + gitResult.getTotalCount());
//            }
//
//            @Override
//            public void onFailure(Call<GitResult> call, Throwable t) {
//                Log.e("retrofit_plus", "failure");
//            }
//
//            @Override
//            public void onStart() {
//                Log.e("retrofit_plus", "start");
//            }
//
//            @Override
//            public void onFinish() {
//                Log.e("retrofit_plus", "finish");
//            }
//
//        });

        RestApi.getApiService(this).getUsersByName2("Mason",new RetrofitPlusCallBack<GitResult>() {

            @Override
            public void onCallStart() {
                Log.e("retrofit_plus", "start");
            }

            @Override
            public void onCallFinish() {
                Log.e("retrofit_plus", "finish");
            }

            @Override
            public void onHttpSuccess(Call<GitResult> call, Response<GitResult> response) {
                GitResult gitResult = response.body();
                Log.e("retrofit_plus", "success:" + response.raw().request().url().toString() + "\n"
                        + gitResult.getTotalCount());
            }

            @Override
            public void onHttpFailure(Call<GitResult> call, Response<GitResult> response) {

            }

            @Override
            public void onNetFailure(Call<GitResult> call, Throwable t) {

            }

        });

//        RestApi.getApiService(this).testHttps(new Callback<HttpsResult>() {
//            @Override
//            public void onResponse(Call<HttpsResult> call, Response<HttpsResult> response) {
//                HttpsResult gitResult = response.body();
//                Log.e("retrofit_plus", "success:" + response.raw().request().url().toString() + "\n"
//                        + gitResult.getCode());
//            }
//
//            @Override
//            public void onFailure(Call<HttpsResult> call, Throwable t) {
//                Log.e("retrofit_plus", "failure:");
//            }
//
//            @Override
//            public void onCallStart() {
//
//            }
//
//            @Override
//            public void onCallFinish() {
//
//            }
//        });
    }
}
