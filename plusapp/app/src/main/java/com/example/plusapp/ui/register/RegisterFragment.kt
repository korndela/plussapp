package com.example.plusapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.plusapp.databinding.RegisterFragmentBinding
import com.example.plusapp.repository.AuthRepository
import com.example.plusapp.viewModel.RegisterViewModel

class RegisterFragment : Fragment() {


    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var nameText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterFragmentBinding.inflate(inflater)

        emailText = binding.registerEditTextEmail
        passwordText = binding.registerEditTextPassword
        nameText = binding.registerEditTextTextPersonName

        initRegisterFormChangeHandler()

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authRepository = AuthRepository()
        val activity = requireNotNull(this.activity) {

        }
        viewModel = ViewModelProvider(
            this,
            RegisterViewModel.Factory(activity.application, authRepository)
        ).get(RegisterViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.fireBaseAuthResult.observe(viewLifecycleOwner, Observer {

            if (it.success != null) {

                updateUiForLoggedInUser()

            } else if (it.error != null)
                showLoginFailed(it.error)

        })

        viewModel.registerFormState.observe(viewLifecycleOwner, Observer { registerFormState ->

            binding.registerFlowButton1.isEnabled = registerFormState.isDataValid

            if (registerFormState.useremailError != null) {

                emailText.error = getString(registerFormState.useremailError)

            }
            if (registerFormState.passwordError != null) {

                passwordText.error = getString(registerFormState.passwordError)

            } else if (registerFormState.usernameError != null) {

                nameText.error = getString(registerFormState.usernameError)
            }

        })


    }

    private fun updateUiForLoggedInUser() {
        this.findNavController()
            .navigate(RegisterFragmentDirections.actionRegisterFragmentToDrinksFragment2())
    }

    private fun showLoginFailed(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    private fun initRegisterFormChangeHandler() {
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