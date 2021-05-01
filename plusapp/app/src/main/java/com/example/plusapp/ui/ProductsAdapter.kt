package com.example.plusapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.plusapp.databinding.ProductItemBinding
import com.example.plusapp.model.Product


class ProductsAdapter(val clickListener: ProductClickListener) :
    ListAdapter<Product, ProductsAdapter.ProductViewHolder>(ProductsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ProductViewHolder(private var itemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(data: Product, clickListener: ProductClickListener) {
            itemBinding.productItem = data
            itemBinding.addPointClickListener = clickListener
            itemBinding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ProductViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductItemBinding.inflate(layoutInflater, parent, false)
                return ProductViewHolder(binding)
            }
        }

    }

}

class ProductsDiffCallback() : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

class ProductClickListener(
    val addPointClickListener: (product: Product) -> Unit,
    val removePointClickListener: (product: Product) -> Unit
) {
    fun addPoint(product: Product) = addPointClickListener(product)
    fun removePoint(product: Product) = removePointClickListener(product)
}