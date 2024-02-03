package com.example.shoppie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppie.R
import com.example.shoppie.data.OnboardingCarouselModel
import com.example.shoppie.databinding.CellOnboardingLayoutBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.CarouselViewHolder>() {
    var list: MutableList<OnboardingCarouselModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = CellOnboardingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CarouselViewHolder(private val binding: CellOnboardingLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingCarouselModel) {
            binding.apply {
                Glide.with(root)
                    .load(item.image)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivPics)

            }
        }
    }
}