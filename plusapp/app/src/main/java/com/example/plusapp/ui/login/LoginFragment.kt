package com.example.plusapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.plusapp.R
import com.example.plusapp.databinding.LoginFragmentBinding
import com.example.plusapp.repository.AuthRepository
import com.example.plusapp.viewModel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        emailText = binding.editTextEmail
        passwordText = binding.editTextPassword

        emailText.doAfterTextChanged {
            viewModel.loginTextValidation(
                it.toString(),
                passwordText.text.toString()
            )
        }

        passwordText.doAfterTextChanged {
            viewModel.loginTextValidation(
                emailText.text.toString(),
                it.toString()
            )
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authRepository = AuthRepository()
        val activity = requireNotNull(this.activity) {

        }
        viewModel = ViewModelProvider(
            this,
            LoginViewModel.Factory(activity.application, authRepository)
        ).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.fireBaseAuthResult.observe(viewLifecycleOwner, Observer {

            if (it.success != null) {

                updateUiForLoggedInUser()

            } else if (it.error != null)
                showLoginFailed(it.error)

        })

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer { loginFromState ->

            binding.LoginButton.isEnabled = loginFromState.isDataValid

            if (loginFromState.usernameError != null) {

                emailText.error = getString(loginFromState.usernameError)

            } else if (loginFromState.passwordError != null) {

                passwordText.error = getString(loginFromState.passwordError)

            }

        })
        viewModel.navigateRegisterScreen.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
                viewModel.navigateEventRegisterScreenDone()
            }
        })
    }

    private fun updateUiForLoggedInUser() {
        this.findNavController()
            .navigate(LoginFragmentDirections.actionLoginFragmentToDrinksFragment2())
    }

    private fun showLoginFailed(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}