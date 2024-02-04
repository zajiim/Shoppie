package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecomapp.models.CarouselModel
import com.example.shoppie.R
import com.example.shoppie.databinding.CellMainCarouselLayoutBinding

class CarouselAdapter : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    var list: MutableList<CarouselModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = CellMainCarouselLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CarouselViewHolder(private val binding: CellMainCarouselLayoutBinding): ViewHolder(binding.root) {
        fun bind(item: CarouselModel) {
            binding.apply {
                tvDescription.text = item.title
                tvSubDescription.text = item.subtitle
                Glide.with(root)
                    .load(item.image)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivCarousel)


                Glide.with(root)
                    .load(item.drawable)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivCarouselForeground)
            }
        }
    }
}