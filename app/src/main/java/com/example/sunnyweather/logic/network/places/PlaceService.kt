package com.example.sunnyweather.logic.network.places

import com.example.sunnyweather.Myapplication
import com.example.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//retrofit命名 以功能开头service结尾
interface  PlaceService{
    //请求地址的路径 （相对）
    @GET("v2/place?token=${Myapplication.TOKEN}&lang=zh_CN")
    //返回值为call类型，并通过泛型指定
    //@Query路径进行补充 value为动态地址中的键值对的键值
    fun searchPlaces(@Query("query") query:String): Call<PlaceResponse>
}