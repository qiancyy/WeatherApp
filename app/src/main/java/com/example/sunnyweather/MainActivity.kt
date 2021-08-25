package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.sunnyweather.ui.place.PlaceFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentFragment=supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment==null){
            var fragment=PlaceFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()
        }
    }
}


//use "Myapplication.context" to get the context
class Myapplication:Application(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var  context: Context
        const val TOKEN="fTn4mb0CP1KZENxh"
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext  //call the getApplicationContext()
    }
}