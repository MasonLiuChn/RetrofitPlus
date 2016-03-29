RetrofitPlus
========
A extension for retrofit

Feature
--------

1、rebase to lastest version of retrofit,support all feature of retrofit.

2、@HTTPS Annotation：config which service use https.

3、add method to set ssl file，put the .pem file into assets folder.

4、add onCallStart(),onCallFinish() to Callback.

5、compatible with Retrofit 1.x,like: put callback in parameter and enqueue directly.
```java
        @HTTPS
        @Headers("User-Agent: Retrofit2.0Tutorial-App")
        @GET("/search/users")
        void getUsersByName2(@Query("q") String name, Callback<GitResult> callback);
```
6、add RetrofitPlusCallBack
```java
        onCallStart();
        onCallFinish();
        onHttpSuccess(Call<T> call, Response<T> response);
        onHttpFailure(Call<T> call, Response<T> response);
        onNetFailure(Call<T> call, Throwable t);
```

中文：

1、已经 rebase 到最新 retrofit 代码，支持原生 retrofit 所有特性。

2、增加一个@HTTPS Annotation，可以灵活配置哪些方法使用 https。

3、封装了一个方法用于配置 OkHttpClient 的 SSL Certificate，只需要配置证书文件名即可（如xxx.pem，将 xxx.pem文件放在 assets 文件夹下）。

4、在 Callback 里增加 onCallStart()、onCallFinish() 回调方法。

5、兼容 Retrofit 1.x，可以把 callback 放到参数里，执行方法后直接发送异步请求，此时方法返回值必须void。
 例如：
```java
         @HTTPS
         @Headers("User-Agent: Retrofit2.0Tutorial-App")
         @GET("/search/users")
         void getUsersByName2(@Query("q") String name, Callback<GitResult> callback);
```
6、添加 RetrofitPlusCallBack 类，回调方法更清晰
```java
        onCallStart();
        onCallFinish();
        onHttpSuccess(Call<T> call, Response<T> response);
        onHttpFailure(Call<T> call, Response<T> response);
        onNetFailure(Call<T> call, Throwable t);
```
Usage
--------

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
dependencies {
	compile 'com.github.MasonLiuChn:RetrofitPlus:2.0.0.3'
}
```
Demo：https://github.com/MasonLiuChn/RetrofitPlus/tree/master/retrofit-plus-demo

Usage of retrofit2.0：https://realm.io/cn/news/droidcon-jake-wharton-simple-http-retrofit-2

#Contact me:

- Blog:http://blog.csdn.net/masonblog

- Email:MasonLiuChn@gmail.com

License
=======

    Copyright 2016 MasonLiu, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
