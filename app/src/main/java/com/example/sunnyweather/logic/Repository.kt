package com.example.sunnyweather.logic
import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.await
import kotlin.coroutines.CoroutineContext

//仓库层


//viewModel 帮助activity存放与界面相关的数据信息  livedata与viewModel使用,将数据变化通知给activity
object Repository {
    fun searchPlaces(query:String)= fire(Dispatchers.IO) { //提供挂起函数的上下文
            val placeresponse= SunnyWeatherNetwork.searchPlaces(query)
            if(placeresponse.status=="ok"){
                val places=placeresponse.places   //返回的call是<PlaceResponse>类型
                Result.success(places)
            }else {
                Result.failure(RuntimeException("response status is ${placeresponse.status}"))
            }
    }

    fun refreshWeather(lng:String,lat:String)= fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
                val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    var weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(RuntimeException("response status is ${realtimeResponse.status}" + "  ${dailyResponse.status}"))
                }
            }
        }
    fun <T> fire(context: CoroutineContext, block:suspend()->Result<T>)= liveData<Result<T>>(context) {
        var result=try {
            block()
        }
        catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}