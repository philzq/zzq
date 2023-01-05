

## 官方网站
|id|desc|url|
|---|----|----|
|1|官网|https://square.github.io/okhttp/|
|2|官方例子|https://github.com/square/okhttp/tree/master/samples/guide/src/main/java/okhttp3/recipes|


## 使用方法
```java
//同步调用
 httpClient.newCall(request).execute(); 

 //异步调用
 httpClient.newCall(request).enqueue(new Callback() {})
 ```
 
