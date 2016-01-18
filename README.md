RetrofitPlus
========
A extension for retrofit

Feature
--------

1、rebase to lastest version of retrofit,support all feature of retrofit.

2、@HTTPS Annotation：config which service use https.

3、add method to set ssl file.

4、add onStart(),onFinish() to Callback.


中文：

1、已经 rebase 到最新 retrofit 代码，支持原生 retrofit 所有特性。

2、增加一个@HTTPS Annotation，可以灵活配置哪些方法时候 https。

3、封装了一个方法用于配置 OkHttpClient 的 SSL Certificate，只需要配置证书文件路径即可。

4、在 Callback 里增加 onStart()、onFinish() 回调方法。

Usage
--------

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
dependencies {
	        compile 'com.github.MasonLiuChn:RetrofitPlus:2.0.0-bata3'
}
```


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
