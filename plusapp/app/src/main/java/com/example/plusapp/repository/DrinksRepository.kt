package com.example.plusapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plusapp.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

const val TAG = "ShopRepository"

class DrinksRepository() {

    private val _drinksList = MutableLiveData<ArrayList<Product>>()
    val drinksList: LiveData<ArrayList<Product>> = _drinksList

//    private val _domainUser = MutableLiveData<DomainUserResult>()
//    val domainUser: LiveData<DomainUserResult> = _domainUser

    private val _isPointAdded = MutableLiveData<Long>()
    val isPointAdded: LiveData<Long> = _isPointAdded

    private val _isPointRemoved = MutableLiveData<Long>()
    val isPointRemoved: LiveData<Long> = _isPointRemoved

    private val _isUserLoggedIn = MutableLiveData<String?>()
    var isUserLoggedIn: LiveData<String?> = _isUserLoggedIn

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        getAllDrinks()
    }

    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    fun getAllDrinks() {


        val currentUserId = getCurrentUserId()
        if (currentUserId != null) {


            firestore.collection("drinks").addSnapshotListener { docSnapshot, e ->
                //Exception hata var
                if (e != null) {

                    Log.w(TAG, e.message.toString())
                    return@addSnapshotListener

                }

                //Exception olmadigi icin document vardir
                if (docSnapshot != null) {
                    //snapshoti gercekleyecegiz
                    val allProduct = ArrayList<Product>()
                    docSnapshot.documents.forEach {
                        val currentProduct = it.toObject(Product::class.java)

                        it?.let {
                            allProduct.add(currentProduct!!)
                        }
                    }
                    _drinksList.value = allProduct
                }
            }
        }
    }

    fun addPointToUser(product: Product) {
        //User Point Added Locally
        val currentUserId = getCurrentUserId()
        if (currentUserId != null) {

//            FirebaseAuth.getInstance().currentUser?.updateEmail("emaildegisti@gmail.com")
//            FirebaseAuth.getInstance().currentUser?.updatePassword()
            firestore.collection("users")
                .document(currentUserId)
                .update("userPoint", FieldValue.increment(product.productBonus))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    _isPointAdded.value = product.productBonus

                }
                .addOnFailureListener { e -> Log.d(TAG, "DocumentSnapshot ${e.message}!") }
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
                                .update(
                                    "userPoint",
                                    FieldValue.increment(-product.productPoint)
                                )
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot successfully written!")
                                    _isPointRemoved.value = product.productPoint
                                }
                                .addOnFailureListener { e ->
                                    Log.d(TAG, "DocumentSnapshot ${e.message}!")
                                }
                        } else {
                            _isPointRemoved.value = (-1).toLong()
                        }
                    }
                }
        }
    }
}
