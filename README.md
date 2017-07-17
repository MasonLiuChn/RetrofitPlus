RetrofitPlus
========
A extension for retrofit2

Feature
--------

1、Rebase to lastest version of retrofit2(now is retrofit-2.3.0),support all feature of retrofit2.

2、@HTTPS Annotation：config which service use https.

Example:
```java
@HTTPS
@GET("/search/users")
Call<GitResult> getUsersByName(@Query("q") String name);
```

3、Set certificate pinning for https of okhttp3.

Example:
* put certificate file into assets folder
```java
OkHttpClientUtil.getSSLClient(okClient, context, "xxx.pem");
```
* set certificate string  directly
```java
OkHttpClientUtil.getSSLClientByCertificateString(okClient,  certificateString);
```
4、Compatible with Retrofit 1.x,like: put callback into parameter and enqueue directly.

Example:
```java
@HTTPS
@GET("/search/users")
void getUsersByName2(@Query("q") String name, Callback<GitResult> callback);
```
5、Add RetrofitPlusCallBack
```java
onCallStart();
onCallFinish();
onHttpSuccess(Call<T> call, Response<T> response);
onHttpFailure(Call<T> call, Response<T> response);
onNetFailure(Call<T> call, Throwable t);
```
6、OkHttpClientUtil.getHeaderValueEncoded 、 OkHttpClientUtil.getHeaderNameEncoded

handle Unexpected char  in header name and value


中文：
---------

1、已经 rebase 到最新 retrofit2 代码(目前是最新的retrofit2.3.0)，支持原生 retrofit2 所有特性。

2、增加@HTTPS Annotation，可以灵活配置哪些方法使用 https。

例如:
```java
@HTTPS
@GET("/search/users")
Call<GitResult> getUsersByName(@Query("q") String name);
```

3、为 okhttp3 的 https 设置certificate pinning。

例如:
* 将证书文件放入assets文件夹
```java
OkHttpClientUtil.getSSLClient(okClient, context, "xxx.pem");
```
* 直接设置证书字符串
```java
OkHttpClientUtil.getSSLClientByCertificateString(okClient,  certificateString);
```

4、兼容 Retrofit 1.x，可以把 callback 放到参数里，执行方法后直接发送异步请求，此时方法返回值必须void。

 例如：
```java
@GET("/search/users")
void getUsersByName2(@Query("q") String name, Callback<GitResult> callback);
```
5、添加 RetrofitPlusCallBack 类，回调方法更完整清晰
```java
onCallStart();
onCallFinish();
onHttpSuccess(Call<T> call, Response<T> response);
onHttpFailure(Call<T> call, Response<T> response);
onNetFailure(Call<T> call, Throwable t);
```
6、OkHttpClientUtil.getHeaderValueEncoded 、 OkHttpClientUtil.getHeaderNameEncoded

当okhttp header 有中文字符时 采用getHeaderValueEncoded 和 getHeaderNameEncoded 进行编码

Usage
--------

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
dependencies {
	compile 'com.github.MasonLiuChn:RetrofitPlus:2.3.0.1'
	compile('com.squareup.retrofit2:converter-gson:2.0.0') {
        	//exclude module: 'retrofit' 如果不写 group 则生成 pom 不会 add exclusion
        	exclude group: 'com.squareup.retrofit2', module: 'retrofit'
    	}
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
