package com.example.sunnyweather.ui.place

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment:Fragment(){
    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }
    private lateinit var adapter:placeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager=LinearLayoutManager(activity)
        recycleView.layoutManager=layoutManager
        adapter= placeAdapter(this,viewModel.placeList)
        recycleView.adapter=adapter
        searchplaceEdit.addTextChangedListener{ edit->
            val content=edit.toString()
            if(content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            }else{
                recycleView.visibility=View.GONE
                bgImage.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
            viewModel.placeLiveData.observe(this, Observer {result->
                val places=result.getOrNull()
                if(places!=null){
                    recycleView.visibility=View.VISIBLE
                    bgImage.visibility=View.GONE
                    viewModel.placeList.clear()
                    viewModel.placeList.addAll(places)
                    adapter.notifyDataSetChanged()
                }
                else {
                    Toast.makeText(activity,"fail to find the location",Toast.LENGTH_LONG).show()
                    result.exceptionOrNull()?.printStackTrace()
                }


            })
        }

    }
}