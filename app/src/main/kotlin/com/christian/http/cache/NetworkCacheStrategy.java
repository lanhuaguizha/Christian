package com.christian.http.cache;

import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

/**
 * 先进行网络请求，如果网络请求失败则直接请求缓存数据
 */
public class NetworkCacheStrategy implements BaseRequestStrategy, HttpLoggingInterceptor.Logger {

    private static final String TAG = NetworkCacheStrategy.class.getSimpleName();

    /**
     * 请求策略
     *
     * @param chain
     * @return
     */
    @Override
    public Response request(Interceptor.Chain chain) throws IOException {
        Response response = null;
        CacheStrategy cacheStrategy = new CacheStrategy();
        NetworkStrategy networkStrategy = new NetworkStrategy();
        try {
            Log.d(TAG, "cache_log " + "request network");
            response = networkStrategy.request(chain);
            if (!response.isSuccessful()) {
                return cacheStrategy.request(chain);
            }
        } catch (Exception e) {
            Log.d(TAG, "cache_log " + e.toString());
            try {
                Log.d(TAG, "cache_log " + "request cache");
                cacheStrategy.setForceReadCache(true);
                response = cacheStrategy.request(chain);
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
