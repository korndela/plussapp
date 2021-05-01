package com.example.plusapp.ui.drinks

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.plusapp.R
import com.example.plusapp.databinding.DrinksFragmentBinding
import com.example.plusapp.repository.DrinksRepository
import com.example.plusapp.ui.ProductClickListener
import com.example.plusapp.ui.ProductsAdapter
import com.example.plusapp.viewModel.DrinksViewModel
import com.google.firebase.auth.FirebaseAuth

class DrinksFragment : Fragment() {

    private lateinit var binding: DrinksFragmentBinding
    private lateinit var viewModel: DrinksViewModel
    private lateinit var drinksRepository: DrinksRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DrinksFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        drinksRepository = DrinksRepository()
        val activity = requireNotNull(this.activity) {
        }

        viewModel = ViewModelProvider(
            this,
            DrinksViewModel.Factory(activity.application, drinksRepository)
        ).get(DrinksViewModel::class.java)

        viewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val welcomeTost = Toast.makeText(
                    context,
                    "Welcome Dear ${FirebaseAuth.getInstance().currentUser?.displayName}",
                    Toast.LENGTH_LONG
                )
                welcomeTost.setGravity(Gravity.TOP or Gravity.CENTER, 0, 0)
                welcomeTost.show()

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.drinksRecyclerView.adapter = ProductsAdapter(
            ProductClickListener(
                { product -> viewModel.addPointToUser(product) },
                { product -> viewModel.removePointFromUser(product) })
        )

    }


}