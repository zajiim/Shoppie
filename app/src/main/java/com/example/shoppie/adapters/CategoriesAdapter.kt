package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecomapp.models.CategoriesModel
import com.example.shoppie.R
import com.example.shoppie.databinding.CellCategoriesLayoutBinding

class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    var list: MutableList<CategoriesModel> = mutableListOf()
    inner class CategoriesViewHolder(private val binding: CellCategoriesLayoutBinding): ViewHolder(binding.root) {
        fun bind(item: CategoriesModel) {
            binding.apply {
                tvItemName.text = item.title
                Glide.with(root)
                    .load(item.icon)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivItemImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CategoriesViewHolder {
        val view = CellCategoriesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}