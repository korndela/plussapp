package com.example.plusapp.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.databinding.WalletFragmentBinding
import com.example.plusapp.repository.WalletRepository
import com.example.plusapp.viewModel.WalletViewModel

class WalletFragment : Fragment() {

    private lateinit var viewModel: WalletViewModel
    private lateinit var binding: WalletFragmentBinding
    private lateinit var walletRepository: WalletRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WalletFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        walletRepository = WalletRepository()
        val activity = requireNotNull(this.activity) {

        }
        viewModel = ViewModelProvider(
            this, WalletViewModel.Factory(
                app = activity.application, walletRepository = walletRepository
            )
        ).get(WalletViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

}