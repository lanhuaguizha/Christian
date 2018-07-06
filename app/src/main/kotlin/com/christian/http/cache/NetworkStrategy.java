package com.christian.http.cache;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 仅仅请求网络策略
 */
public class NetworkStrategy implements BaseRequestStrategy {

    private static final float MAX_AGE = 0;
    private float mMaxAge; //表示当访问此网页后的max-age秒内再次访问不会去服务器请求

    public NetworkStrategy() {
        mMaxAge = MAX_AGE;
    }

    public NetworkStrategy(float maxAge) {
        this.mMaxAge = MAX_AGE;
    }

    @Override
    public Response request(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        response = response.newBuilder()
                .addHeader("Cache-Control", "public, max-age=" + mMaxAge)
                .removeHeader("Pragma")
                .build();
        return response;
    }
}
