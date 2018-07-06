package com.christian.http.cache;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * 先进行网络请求，如果网络请求失败则直接请求缓存数据
 */
public class NetworkCacheStrategy implements BaseRequestStrategy {

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
            response = networkStrategy.request(chain);
            if (!response.isSuccessful()) {
                return cacheStrategy.request(chain);
            }
        } catch (Exception e) {
            try {
                response = cacheStrategy.request(chain);
            } catch (IOException e1) {
                //忽略不处理
            }
        }
        return response;
    }
}
