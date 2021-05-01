package com.example.plusapp.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.plusapp.R
import com.example.plusapp.databinding.SettingsFragmentBinding
import com.example.plusapp.model.UserSettings
import com.example.plusapp.repository.SettingRepository
import com.example.plusapp.viewModel.SettingsViewModel

class SettingsFragment : Fragment() {


    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var settingRepository: SettingRepository
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var nameText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater)
        emailText = binding.editTextTextPersonEmail
        passwordText = binding.editTextTextPersonPassword
        nameText = binding.editTextTextPersonName
        initSettingsFormChangeHandler()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingRepository = SettingRepository()
        val activity = requireNotNull(this.activity) {

        }
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.Factory(activity.application, settingRepository)
        ).get(SettingsViewModel::class.java)
        binding.settingsViewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.isSignedOut.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                navigateToLoginScreen()
            }
        })

        viewModel.isUserUpdated.observe(viewLifecycleOwner, Observer {
            val alertDialog: AlertDialog? = activity.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(
                        R.string.ok
                    ) { dialog, id ->
                        // User clicked OK button
                    }
                }.setMessage("Your changes updated")
                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()
        })

        viewModel.settingsForm.observe(viewLifecycleOwner, Observer { form ->

            binding.settingsForm = UserSettings(form.userName, form.userEmail, form.password)

        })

        viewModel.settingsFormState.observe(viewLifecycleOwner, Observer { settingFormState ->

            binding.saveChanges.isEnabled = settingFormState.isDataValid

            if (settingFormState.userEmailError != null) {

                emailText.error = getString(settingFormState.userEmailError)

            }
            if (settingFormState.passwordError != null) {

                passwordText.error = getString(settingFormState.passwordError)

            } else if (settingFormState.userNameError != null) {

                nameText.error = getString(settingFormState.userNameError)
            }

        })

    }

    private fun navigateToLoginScreen() {
        this.findNavController()
            .navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

    private fun initSettingsFormChangeHandler() {
        emailText.doAfterTextChanged {
            viewModel.registerTextValidation(
                it.toString(),
                passwordText.text.toString(),
                nameText.text.toString()
            )
        }

        passwordText.doAfterTextChanged {
            viewModel.registerTextValidation(
                emailText.text.toString(),
                it.toString(),
                nameText.text.toString()
            )
        }
        nameText.doAfterTextChanged {
            viewModel.registerTextValidation(
                emailText.text.toString(),
                passwordText.text.toString(),
                it.toString()
            )
        }
    }

}