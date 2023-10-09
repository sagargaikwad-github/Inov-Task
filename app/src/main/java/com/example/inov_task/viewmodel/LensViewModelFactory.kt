package com.example.inov_task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inov_task.repository.LensRepository

class LensViewModelFactory(private val repository: LensRepository,var num : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LensViewModel(repository,num) as T
    }
}