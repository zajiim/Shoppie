package com.example.shoppie.utils.common

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.example.shoppie.databinding.FragmentModalProgressLoadingBinding
import com.example.shoppie.utils.setCornerRadius

class ModalProgressLoadingFragment : Fragment() {
    private lateinit var binding: FragmentModalProgressLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalProgressLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            indicatorLayout.setCornerRadius()
            logoImage.setCornerRadius()
            root.visibility = View.GONE
        }
    }

    fun showModalProgress() {
        ObjectAnimator.ofFloat(0f, 1f).apply {
            interpolator = OvershootInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Float
                binding.apply {
                    root.visibility = View.VISIBLE
                    modalBackground.alpha = progress
                    indicatorLayout.apply {
                        scaleX = progress
                        scaleY = progress
                    }
                }
            }
            duration = 600L
            start()
        }
    }

    fun hideModalProgress() {
        ObjectAnimator.ofFloat(1f, 0f).apply {
            addUpdateListener {
                val progress = it.animatedValue as Float
                binding.apply {
                    modalBackground.alpha = progress
                    indicatorLayout.apply {
                        scaleX = progress
                        scaleY = progress
                    }
                }
            }
            doOnEnd { binding.root.visibility = View.GONE }
            duration = 600L
            start()
        }
    }
}