package com.christian.data.source.remote

import com.christian.data.Nav
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


interface NavService {

//    @Headers("Cache-Control: max-age=60")
    @GET("nav")
    fun getNavs(): Call<List<Nav>>
}


// tvwall包下的类结构简单解释：View -> Controller -> Service(业务逻辑类，后端也常用我想应该和android Service组件定义不同) -> (Dao/Mapper层已省略) -> Model/Bean(entity包下的javabean都是model类) -> 通过接口更新view
// -> 表示事件流
//interface APIService {
//
//    @GET("list_tv_wall")
//    fun getTvWalls(@Query("categoryId") categoryId: Int?, @Query("version") version: Double?, @Query("format") format: String): Call<TvWallBean>
//}