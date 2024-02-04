package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecomapp.models.CarouselModel2
import com.example.shoppie.R
import com.example.shoppie.databinding.CellItemsCarouselLayoutBinding

class ItemsCarousel: RecyclerView.Adapter<ItemsCarousel.ItemsViewHolder>() {
    var list: MutableList<CarouselModel2> = mutableListOf()
    inner class ItemsViewHolder (private val binding: CellItemsCarouselLayoutBinding): ViewHolder(binding.root){
        fun bind(item: CarouselModel2) {
            binding.apply {
                ivBgDrawable.setBackgroundColor(ContextCompat.getColor(itemView.context, item.color))
                tvItemName.text = item.title
                Glide.with(root)
                    .load(item.image)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivItemImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = CellItemsCarouselLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(list[position])
    }
}