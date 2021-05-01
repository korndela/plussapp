package com.example.plusapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plusapp.model.DomainUser
import com.example.plusapp.model.DomainUserResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WalletRepository() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _domainUser = MutableLiveData<DomainUserResult>()
    val domainUser: LiveData<DomainUserResult> = _domainUser

    init {
        getFirestoreUser()
    }


    fun getFirestoreUser() {
        val uId = getCurrentUserId()
        if (uId != null) {

            firestore.collection("users")
                .document(uId)
                .addSnapshotListener { userSnapsot, e ->

                    if (e != null) {
                        _domainUser.value =
                            DomainUserResult(error = "User infortmation not available know")
                        return@addSnapshotListener
                    }
                    if (userSnapsot != null) {

                        val user = userSnapsot.toObject(DomainUser::class.java)
                        _domainUser.value = DomainUserResult(success = user)
                    }
                }
        }
    }

    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid
}