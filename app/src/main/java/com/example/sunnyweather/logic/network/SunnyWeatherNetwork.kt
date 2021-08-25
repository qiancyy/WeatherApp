package com.example.sunnyweather.logic.network

import com.example.sunnyweather.logic.network.places.PlaceService
import com.example.sunnyweather.logic.network.weather.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//对所有网络请求的API进行封装
//placeservice
object SunnyWeatherNetwork {
    private val placeservice= ServiceCreator.create<PlaceService>()//持有一个接口实例，
    suspend fun searchPlaces(query:String)= placeservice.searchPlaces(query).wait() //函数searchPlaces 调用之前接口定义函数返回call<T>
    private val weatherService=ServiceCreator.create<WeatherService>()
    suspend fun getRealtimeWeather(lng:String,lat:String)= weatherService.getRealtimeWeather(lng,lat).wait()
    suspend fun getDailyWeather(lng:String,lat:String)= weatherService.getDailyWeather(lng,lat).wait()
    private suspend fun<T> Call<T>.wait():T{ //call的扩展函数
        return suspendCoroutine {
            continuation -> enqueue(object:Callback<T>{  //在enqueue中开好了子线程 匿名类做参数用 实现接口函数
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body=response.body() //得到解析后的对象 call<T>类型
                if(body!=null) continuation.resume(body)  //挂起恢复
                else continuation.resumeWithException(
                        RuntimeException("respond body is null")
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
        }
    }

}