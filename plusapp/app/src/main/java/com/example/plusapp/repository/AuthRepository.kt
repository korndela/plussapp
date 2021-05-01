package com.example.plusapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plusapp.model.FirebaseDatabase
import com.example.plusapp.ui.login.LoginResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

enum class AuthStatus { LOADING, DONE, ERROR }

class AuthRepository() {

    val db = FirebaseDatabase()

    private val _fireBaseAuthResult = MutableLiveData<LoginResult>()
    val fireBaseAuthResult: LiveData<LoginResult> = _fireBaseAuthResult
//
//    private val _firebaseRegisterResult = MutableLiveData<RegisterResult>()
//    val firebaseRegisterResult: LiveData<RegisterResult> = _firebaseRegisterResult

    private val _authStatus = MutableLiveData<AuthStatus>()
    val authStatus: LiveData<AuthStatus> = _authStatus


    fun registerUser(email: String, password: String, name: String) {

        _authStatus.value = AuthStatus.LOADING

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { registerResult ->

                try {

                    if (registerResult.isSuccessful) {

                        db.addNewUserToFirestore(registerResult.result?.user!!.uid).let {

                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                val profileChangeRequest =
                                    userProfileChangeRequest { displayName = name }

                                user.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            initUser(registerResult.result!!)
                                        }
                                    }
                            }
                        }
                    } else {
                        Log.i("Auth", registerResult.exception?.message.toString())

                        _fireBaseAuthResult.postValue(LoginResult(error = registerResult.exception?.message))
                        _authStatus.postValue(AuthStatus.ERROR)

                    }
                } catch (e: Exception) {

                    throw e
                }


            }
    }

    fun loginUser(email: String, password: String) {

        _authStatus.postValue(AuthStatus.LOADING)

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { loginResult ->

                if (loginResult.isSuccessful) {

                    initUser(loginResult.result!!)

                } else {

                    if (loginResult.exception != null) {
                        //Error Login
                        Log.i("Auth", loginResult.exception?.message.toString())
                        _authStatus.value = AuthStatus.ERROR
                        _fireBaseAuthResult.postValue(LoginResult(error = loginResult.exception?.message))

                    }

                }

            }

    }

    private fun initUser(registerResult: AuthResult) {
        _fireBaseAuthResult.postValue(LoginResult(success = registerResult.user))
        _authStatus.postValue(AuthStatus.DONE)
    }
}