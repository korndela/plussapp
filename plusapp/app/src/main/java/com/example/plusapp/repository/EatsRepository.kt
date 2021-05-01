package com.example.plusapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plusapp.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class EatsRepository {

    private val _eatsList = MutableLiveData<ArrayList<Product>>()
    val eatsList: LiveData<ArrayList<Product>> = _eatsList

    private val _isPointAdded = MutableLiveData<Long>()
    val isPointAdded: LiveData<Long> = _isPointAdded

    private val _isPointRemoved = MutableLiveData<Long>()
    val isPointRemoved: LiveData<Long> = _isPointRemoved

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        getAllEats()
    }

    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    fun getAllEats() {

        firestore.collection("eats")
            .addSnapshotListener { docSnapshot, e ->
                //Exception varsa hata vardir
                if (e != null) {
                    Log.w(TAG, e.message.toString())
                    return@addSnapshotListener
                }

                //Exception olmadigi icin document vardir
                if (docSnapshot != null) {
                    //snapshoti gercekleyecegiz
                    val allProduct = ArrayList<Product>()
                    docSnapshot.documents.forEach {
                        Log.i("selamlar", "$it")

                        val currentProduct = it.toObject(Product::class.java)

                        it?.let {
                            allProduct.add(currentProduct!!)
                        }
                    }
                    _eatsList.value = allProduct
                }

            }
    }

    fun addPointToUser(product: Product) {
        //User Point Added Locally

        val currentUserId = getCurrentUserId()
        if (currentUserId != null) {

            firestore.collection("users")
                .document(currentUserId)
                .update("userPoint", FieldValue.increment(product.productBonus))
                .addOnSuccessListener {
                    _isPointAdded.value = product.productBonus
                }
        }
    }

    fun removePointFromUser(product: Product) {
        //User Point Added Locally
        val currentUserId = getCurrentUserId()
        if (currentUserId != null) {

            firestore.collection("users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener {

                    val userPoint = it.getLong("userPoint")

                    if (userPoint != null) {
                        if (userPoint >= product.productPoint) {

                            firestore.collection("users")
                                .document(currentUserId)
                                .update("userPoint", FieldValue.increment(-product.productPoint))
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot successfully written!")
                                    _isPointRemoved.value = product.productPoint
                                }
                                .addOnFailureListener { e ->
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot ${e.message}!"
                                    )
                                }
                        } else {
                            _isPointRemoved.value = (-1).toLong()
                        }
                    }
                }
        }
    }

}