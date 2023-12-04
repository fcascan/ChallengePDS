package com.fcascan.challengepds.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fcascan.challengepds.repositories.MainRepository

class MyViewModelFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            MainActivityViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found or Unknown Class")
        }
    }
}