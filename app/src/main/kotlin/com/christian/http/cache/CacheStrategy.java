package com.christian.http.cache;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

/**
 * 仅仅请求缓存策略
 */
public class CacheStrategy implements BaseRequestStrategy, HttpLoggingInterceptor.Logger {

    private static final String TAG = CacheStrategy.class.getSimpleName();

    private static final float MAX_STALE = 60 * 60 * 24 * 30;//过期时间为30天

    /**
     * 请求策略
     *
     * @param chain
     * @return
     */
    @Override
    public Response request(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();//没有网络，直接读取缓存
        Response response = chain.proceed(request);

        String serverCache = response.header("Cache-Control");

        if (TextUtils.isEmpty(serverCache)) { // 如果服务器没有设置缓存策略，则设置为立即请求网络
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                // 如果请求接口中未设置cacheControl，则统一设置为0分钟
                Log.d(TAG, "cache_log " + "public, only-if-cached, max-stale=" + MAX_STALE);
                return response.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
                        .build();
            } else {  // 如果服务端设置相应的缓存策略那么遵从服务端的不做修改
                Log.d(TAG, "cache_log " + "Cache-Control " + cacheControl);
                return response.newBuilder()
                        .addHeader("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        }

        return response;
    }

    @Override
    public void log(String message) {
        Log.d("http_log", message);
    }
}
