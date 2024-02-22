package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shoppie.R
import com.example.shoppie.data.models.MyCartModel
import com.example.shoppie.databinding.ItemMyCartBinding

class MyCartAdapter: RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>() {
    var list: MutableList<MyCartModel> = mutableListOf()

    inner class MyCartViewHolder(private val binding: ItemMyCartBinding): ViewHolder(binding.root) {

        fun bind(item: MyCartModel) {
            binding.apply {
                tvProductName.text = item.itemName
                tvColor.text = item.itemColor
                tvPrice.text = item.itemPrice
                Glide.with(root)
                    .load(item.itemImage)
                    .placeholder(R.drawable.placeholder)
                    .into(ivProductImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val view = ItemMyCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        holder.bind(list[position])
    }
}