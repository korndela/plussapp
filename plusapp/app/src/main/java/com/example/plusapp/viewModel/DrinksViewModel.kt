package com.example.plusapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.model.Product
import com.example.plusapp.repository.DrinksRepository

class DrinksViewModel(app: Application, private val drinksRepository: DrinksRepository) :
    ViewModel() {


    val drinkList: LiveData<ArrayList<Product>> = drinksRepository.drinksList
    val isPointAdded: LiveData<Long> = drinksRepository.isPointAdded
    val isPointRemoved: LiveData<Long> = drinksRepository.isPointRemoved
    var isUserLoggedIn: LiveData<String?> = drinksRepository.isUserLoggedIn


    fun addPointToUser(product: Product) {

        drinksRepository.addPointToUser(product)

    }

    fun removePointFromUser(product: Product) {

        drinksRepository.removePointFromUser(product)

    }

    open class Factory(val app: Application, private val drinksRepository: DrinksRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DrinksViewModel::class.java)) {
                return DrinksViewModel(app, drinksRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel is not assignable")
            }
        }
    }

}