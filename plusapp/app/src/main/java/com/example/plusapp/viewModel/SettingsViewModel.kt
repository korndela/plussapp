package com.example.plusapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.R
import com.example.plusapp.model.UserSettings
import com.example.plusapp.repository.AuthStatus
import com.example.plusapp.repository.SettingRepository
import com.example.plusapp.ui.settings.SettingsForm
import com.example.plusapp.ui.settings.SettingsFormState

class SettingsViewModel(app: Application, private val settingRepository: SettingRepository) :
    ViewModel() {

    private val settingsSettingRepository = settingRepository

    private val _settingsForm = MutableLiveData<SettingsForm>()
    val settingsForm: LiveData<SettingsForm> = _settingsForm

    private val _settingsFormState = MutableLiveData<SettingsFormState>()
    val settingsFormState: LiveData<SettingsFormState> = _settingsFormState

    val isSignedOut: LiveData<Boolean?> = settingsSettingRepository.isSignedOut
    val currentUserSettings: LiveData<UserSettings> = settingsSettingRepository.currentAuthUser

    val updateStatus: LiveData<AuthStatus> = settingsSettingRepository.updateStatus

    val isUserUpdated: LiveData<Boolean> = settingsSettingRepository.isUserUpdated

    fun signedOut() {
        settingsSettingRepository.signOut()
    }

    fun signedOutCompleted() {
        settingsSettingRepository.signedOutCompleted()
    }

    fun updateUserInformation(userSettings: UserSettings) {

        settingsSettingRepository.updateUserInformation(userSettings)
    }

    fun registerTextValidation(username: String, password: String, name: String) {

        if (!isUserNameValid(username)) {

            _settingsFormState.value =
                SettingsFormState(userEmailError = R.string.user_name_is_not_valid)

        } else if (!isPasswordValid(password)) {

            _settingsFormState.value =
                SettingsFormState(passwordError = R.string.password_is_not_valid)

        } else if (!isNameValid(name)) {
            _settingsFormState.value = SettingsFormState(userNameError = R.string.name_is_not_valid)
        } else {

            _settingsFormState.value = SettingsFormState(isDataValid = true)
            _settingsForm.value = SettingsForm(username, password, name)
        }

    }

    private fun isUserNameValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isNameValid(name: String): Boolean {
        return name.length > 2
    }

    open class Factory(val app: Application, private val settingRepository: SettingRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(app, settingRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel is not assignable")
            }
        }
    }
}