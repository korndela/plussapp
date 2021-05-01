package com.example.plusapp.ui.eats

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.R
import com.example.plusapp.databinding.EatsFragmentBinding
import com.example.plusapp.repository.EatsRepository
import com.example.plusapp.ui.ProductClickListener
import com.example.plusapp.ui.ProductsAdapter
import com.example.plusapp.viewModel.EatsViewModel

class EatsFragment : Fragment() {

    private lateinit var viewModel: EatsViewModel
    private lateinit var binding: EatsFragmentBinding
    private lateinit var eatsRepository: EatsRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EatsFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eatsRepository = EatsRepository()
        val activity = requireNotNull(this.activity) {

        }
        viewModel = ViewModelProvider(
            this,
            EatsViewModel.Factory(activity.application, eatsRepository)
        ).get(EatsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isPointRemoved.observe(viewLifecycleOwner, Observer { pointRemoved ->
            if (pointRemoved < 0) {
                val alertDialog: AlertDialog? = activity.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setPositiveButton(
                            R.string.ok
                        ) { dialog, id ->
                            // User clicked OK button

                        }
                    }
                        .setMessage("You Don't Have Enough Points!!! You can check our list again:))) Let's go!!")
                    // Create the AlertDialog
                    builder.create()
                }
                alertDialog?.show()

            } else {
                val alertDialog: AlertDialog? = activity.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setPositiveButton(
                            R.string.ok
                        ) { dialog, id ->
                            // User clicked OK button

                        }
                    }.setMessage("We remove your $pointRemoved Point from your Wallet Enjoy!!!!!!")
                    // Create the AlertDialog
                    builder.create()
                }
                alertDialog?.show()
            }

        })
        viewModel.isPointAdded.observe(viewLifecycleOwner, Observer { pointAdded ->

            val alertDialog: AlertDialog? = activity.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(
                        R.string.ok
                    ) { dialog, id ->
                        // User clicked OK button

                    }
                }
                    .setMessage("Great!!! You Earn $pointAdded Point!!!We added it your Wallet.Enjoy!!!")
                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()
        })

        binding.eatsRecyclerView.adapter = ProductsAdapter(
            ProductClickListener(
                { product -> viewModel.addPointToUser(product) },
                { product ->
                    viewModel.removePointFromUser(product)

                })
        )
    }

}