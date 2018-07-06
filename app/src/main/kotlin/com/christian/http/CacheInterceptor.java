package com.christian.http;

import android.util.Log;
import com.christian.http.cache.*;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * 缓存数据拦截器
 */
public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        RequestStrategy requestStrategy = new RequestStrategy();
        String cacheTypeHeader = String.valueOf(CacheType.NETWORK_ELSE_CACHE);
//        String cacheTypeHeader = chain.request().headers().get(HttpConfig.REQUEST_CACHE_TYPE_HEAD);
        if (cacheTypeHeader != null) {
            int cacheType = Integer.parseInt(cacheTypeHeader);
            Log.e("111", "请求tag:" + cacheType + " 请求url:" + chain.request().url().toString());
            switch (cacheType) {
                case CacheType.ONLY_CACHE:
                    requestStrategy.setBaseRequestStrategy(new CacheStrategy());
                    break;
                case CacheType.ONLY_NETWORK:
                    requestStrategy.setBaseRequestStrategy(new NetworkStrategy());
                    break;
                case CacheType.CACHE_ELSE_NETWORK: // 先读缓存，没有缓存请求网络有缓存永久缓存
                    requestStrategy.setBaseRequestStrategy(new CacheNetworkStrategy());
                    break;
                case CacheType.NETWORK_ELSE_CACHE:
                    requestStrategy.setBaseRequestStrategy(new NetworkCacheStrategy());
                    break;
                case CacheType.INVALID_TYPE:
                    break;
                default:
                    break;
            }
        }
        return requestStrategy.request(chain);
    }
}
