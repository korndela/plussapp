package com.example.plusapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.model.DomainUserResult
import com.example.plusapp.repository.WalletRepository

class WalletViewModel(app: Application, private val walletRepository: WalletRepository) :
    ViewModel() {
    // TODO: Implement the ViewModel

    val userResult: LiveData<DomainUserResult> = walletRepository.domainUser

    open class Factory(val app: Application, val walletRepository: WalletRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
                return WalletViewModel(app, walletRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel is not assignable")
            }
        }
    }


}