package com.christian.http.cache;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

public interface BaseRequestStrategy extends HttpLoggingInterceptor.Logger {

    /**
     * 请求策略
     *
     * @param chain
     * @return
     * @throws IOException
     */
    Response request(Interceptor.Chain chain) throws IOException;
}
