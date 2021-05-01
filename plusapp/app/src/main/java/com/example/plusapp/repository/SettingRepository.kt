package com.example.plusapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plusapp.model.UserSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingRepository {

    private val _isSignedOut = MutableLiveData<Boolean?>()
    val isSignedOut: LiveData<Boolean?> = _isSignedOut

    private val _updateStatus = MutableLiveData<AuthStatus>()
    val updateStatus: LiveData<AuthStatus> = _updateStatus

    private val _currentAuthUser = MutableLiveData<UserSettings>()
    val currentAuthUser: LiveData<UserSettings> = _currentAuthUser

    private val _isUserUpdated = MutableLiveData<Boolean>()
    val isUserUpdated: LiveData<Boolean> = _isUserUpdated

    init {
        settingsUserInit()
    }

    fun updateUserInformation(newUser: UserSettings) {


        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in

            _updateStatus.value = AuthStatus.LOADING

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(newUser.userDisplayName).build()

            user.updateProfile(profileChangeRequest)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        user.updateEmail(newUser.userEmail)
                            .addOnSuccessListener {
                                user.updatePassword(newUser.userPassword)
                                    .addOnSuccessListener {
                                        _isUserUpdated.value = true
                                        _updateStatus.postValue(AuthStatus.DONE)
                                    }
                            }
                    } else {
                        Log.i("Settings Repository", it.exception.toString())
                        _updateStatus.value = AuthStatus.ERROR
                    }
                }

        } else {
            _currentAuthUser.value = UserSettings("Ooops", "Ooops")
        }



    }

    fun settingsUserInit() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            _currentAuthUser.value =
                UserSettings(userDisplayName = user.displayName!!, userEmail = user.email!!)
            // User is signed in
        } else {
            _currentAuthUser.value = UserSettings("Ooops", "Ooops")
        }

    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _isSignedOut.value = true
    }

    fun signedOutCompleted() {
        _isSignedOut.value = null
    }

}