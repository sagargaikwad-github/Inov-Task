package com.example.inov_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.inov_task.model.Attribute

import com.example.inov_task.model.LensCollection
import com.example.inov_task.model.ProductInfo
import com.example.inov_task.repository.LensRepository

class LensViewModel(val lensRepository: LensRepository, var num: Int) : ViewModel() {


    lateinit var lensList: LiveData<LensCollection>
    var totalProducts = -1

    init {
        lensList = lensRepository.loadData("en", "KWD")
        totalProducts = num
    }

    //We only access parent LensCollection data and other data get from iterating LensCollection.
    fun getLenses(): LiveData<LensCollection> {
        return lensList
    }


    fun minusFromCart(): Int {
        if (totalProducts > 1) {
            return --totalProducts
        } else {
            return totalProducts
        }
    }

    fun addToCart(): Int {
        return ++totalProducts
    }


}