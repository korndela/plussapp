package com.example.plusapp.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.plusapp.R
import com.example.plusapp.model.Product
import com.example.plusapp.repository.AuthStatus
import com.example.plusapp.ui.ProductsAdapter

@BindingAdapter("imgUrl")
fun setUrltoImage(imageView: ImageView, url: String?) {
    url?.let {

        val imgUrl = url.toUri().buildUpon().scheme("https").build()

        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}

@BindingAdapter("registerStatus")
fun setProgressBarStatus(view: View, status: AuthStatus?) {
    when (status) {
        AuthStatus.LOADING -> view.visibility = View.VISIBLE
        AuthStatus.ERROR-> view.visibility = View.GONE
        else -> view.visibility = View.GONE
    }
}

@BindingAdapter("submitList")
fun setRecyclerViewListData(recyclerView: RecyclerView, listData: List<Product>?) {
    listData?.let {
        val adapter = recyclerView.adapter as ProductsAdapter
        adapter.submitList(listData)
    }
}

@BindingAdapter("textWithPoint")
fun setTextWithPoint(textView: TextView, number: Long?) {
    number?.let {

        textView.text = "Buy +$number point"

    }
}

@BindingAdapter("textWithBonusPoint")
fun setTextWithBonusPoint(textView: TextView, number: Long?) {
    number?.let {

        textView.text = "Use Bonus -$number bonus"

    }
}

@BindingAdapter("textWithDolarSign")
fun setTextWithDolarSign(textView: TextView, number: Long?) {
    number?.let {

        textView.text = "$number $"

    }
}