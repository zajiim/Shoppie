package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shoppie.data.models.FeaturedProductModel
import com.example.shoppie.R
import com.example.shoppie.databinding.CellFeaturedProductsBinding

class FeaturedProductsAdapter: RecyclerView.Adapter<FeaturedProductsAdapter.FeaturedProductsViewHolder>() {
    var list: MutableList<FeaturedProductModel> = mutableListOf()
    lateinit var onClick: (FeaturedProductModel) -> Unit
    inner class FeaturedProductsViewHolder(private val binding: CellFeaturedProductsBinding): ViewHolder(binding.root) {

        fun bind(item: FeaturedProductModel) {
            binding.apply {
                tvItemName.text = item.productName
                tvItemPrice.text = item.price
                tvReviews.text = item.reviewCounts
                tvStarCount.text = item.rating.toString()
                Glide.with(root)
                    .load(item.image)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivImage)

                root.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedProductsViewHolder {
        val view = CellFeaturedProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturedProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FeaturedProductsViewHolder, position: Int) {
        holder.bind(list[position])
    }
}