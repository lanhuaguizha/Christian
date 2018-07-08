package com.christian.http.cache;

import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

/**
 * 读取缓存，如果缓存不存在则读取网络
 */
public class CacheNetworkStrategy implements BaseRequestStrategy, HttpLoggingInterceptor.Logger {

    private static final String TAG = CacheNetworkStrategy.class.getSimpleName();

    /**
     * 请求策略 ,异常直接在里面捕获，不直接抛出
     *
     * @param chain
     * @return
     */
    @Override
    public Response request(Interceptor.Chain chain) {
        Response response = null;
        CacheStrategy cacheStrategy = new CacheStrategy();
        NetworkStrategy networkStrategy = new NetworkStrategy();
        NetworkCacheStrategy networkCacheStrategy = new NetworkCacheStrategy();
        try {
            Log.d(TAG, "cache_log " + "request cache");
            response = cacheStrategy.request(chain);
            Log.d(TAG, "cache_log " + "mForceReadCache " + cacheStrategy.mForceReadCache);
            if (response.code() == 504 && !cacheStrategy.mForceReadCache) {
                Log.d(TAG, "cache_log " + "response.code() " + 504);
                return networkStrategy.request(chain);
            }
        } catch (Exception e) {
            Log.d(TAG, "cache_log " + e.toString());
            try {
                Log.d(TAG, "cache_log " + "request network_cache");
                response = networkCacheStrategy.request(chain);
            } catch (IOException e1) {
                //忽略不处理
            }
        }
        return response;
    }

    @Override
    public void log(String message) {
        Log.d("http_log", message);
    }
}
