package com.example.plusapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.model.Product
import com.example.plusapp.repository.EatsRepository

class EatsViewModel(app: Application, val eatsRepository: EatsRepository) : ViewModel() {


    val eatsList: LiveData<ArrayList<Product>> = eatsRepository.eatsList
    val isPointAdded: LiveData<Long> = eatsRepository.isPointAdded
    val isPointRemoved: LiveData<Long> = eatsRepository.isPointRemoved


    fun addPointToUser(product: Product) {

        eatsRepository.addPointToUser(product)

    }

    fun removePointFromUser(product: Product) {

        eatsRepository.removePointFromUser(product)

    }

    open class Factory(val app: Application, private val eatsRepository: EatsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EatsViewModel::class.java)) {
                return EatsViewModel(app, eatsRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel is not assignable")
            }
        }
    }
}