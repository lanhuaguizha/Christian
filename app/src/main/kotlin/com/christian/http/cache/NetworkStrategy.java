package com.christian.http.cache;


import android.text.TextUtils;
import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

/**
 * 仅仅请求网络策略
 */
public class NetworkStrategy implements BaseRequestStrategy, HttpLoggingInterceptor.Logger {

    private static final String TAG = NetworkStrategy.class.getSimpleName();

    @Override
    public Response request(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        // bug: unexpected end of stream on Connection{192.168.0.193:8080, proxy=DIRECT hostAddress=/192.168.0.193:8080 cipherSuite=none protocol=http/1.1
        request = request.newBuilder()
                .addHeader("Connection", "close")
                .build();

        Response response = chain.proceed(request);

        String serverCache = response.header("Cache-Control");
        // 如果服务端设置相应的缓存策略那么遵从服务端的不做修改
        if (TextUtils.isEmpty(serverCache)) {
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                // 如果请求接口中未设置cacheControl，则统一设置为0分钟
                int maxAge = 0;
                Log.d(TAG, "cache_log" + "public, max-age=" + maxAge);
                return response.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                Log.d(TAG, "cache_log" + "Cache-Control: max-age= " + cacheControl);
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .addHeader("Cache-Control", cacheControl)
                        .build();
            }
        }

//        response = response.newBuilder()
//                .addHeader("Cache-Control", "public, max-age=" + mMaxAge)
//                .removeHeader("Pragma")
//                .build();
        return response;
    }

    @Override
    public void log(String message) {
        Log.d("http_log", message);
    }
}
