package com.example.plusapp.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDatabase() {

    private var db: FirebaseFirestore = Firebase.firestore


    fun addNewUserToFirestore(userId: String): Boolean {
        var isUserImplemented: Boolean = false

        db.collection("users").document(userId)
            .get()
            .addOnCompleteListener {

                if (it.result?.data == null) {

                    val data = hashMapOf(
                        "userId" to userId,
                        "userPoint" to 5
                    )

                    db.collection("users").document(userId).set(data)
                        .addOnSuccessListener {
                            isUserImplemented = true
                            Log.i("FirebaseDatabase", "User Successfully implemented")

                        }
                        .addOnFailureListener {
                            Log.i("FirebaseDatabase", "User UnSuccessfully implemented ${it}")
                        }

                }
            }
        return isUserImplemented
    }


}