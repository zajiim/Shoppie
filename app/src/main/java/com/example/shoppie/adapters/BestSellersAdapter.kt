package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shoppie.data.models.BestSellersModel
import com.example.shoppie.R
import com.example.shoppie.databinding.CellBestSellersLayoutBinding

class BestSellersAdapter: RecyclerView.Adapter<BestSellersAdapter.BestSellersViewHolder>() {
    var list: MutableList<BestSellersModel> = mutableListOf()
    inner class BestSellersViewHolder(private val binding: CellBestSellersLayoutBinding): ViewHolder(binding.root) {

        fun bind(item: BestSellersModel) {
            binding.apply {
                tvItemName.text = item.productName
                tvItemPrice.text = item.price
                tvReviews.text = item.reviewCounts
                tvStarCount.text = item.rating.toString()
                Glide.with(root)
                    .load(item.image)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSellersViewHolder {
        val view = CellBestSellersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestSellersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BestSellersViewHolder, position: Int) {
        holder.bind(list[position])
    }
}