package com.example.shoppie.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.shoppie.R
import com.example.shoppie.adapters.OnboardingAdapter
import com.example.shoppie.data.OnboardingCarouselModel
import com.example.shoppie.databinding.FragmentOnboardingBinding
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private val onboardingAdapter = OnboardingAdapter()
    private lateinit var autoScrollJob: Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupWindowInsets()
    }

    private fun setupWindowInsets() {
        requireMainActivity().windowsInsetsLiveData.observe(viewLifecycleOwner) { insets ->
            val rect = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )
            binding.safeTopGuideline.setGuidelineBegin(rect.top)
            binding.safeBottomGuideline.setGuidelineEnd(rect.bottom)
        }
    }

    private fun setupViews() = binding.apply {
        btnCreateAccount.setCornerRadius(24f)
        setUpViewPager()
    }

    private fun setUpViewPager() = binding.apply {

        val onboardingImageList = mutableListOf<OnboardingCarouselModel>().apply {
            add(
                OnboardingCarouselModel(
                    id = "1",
                    image = R.drawable.pic1,
                )
            )
            add(
                OnboardingCarouselModel(
                    id = "2",
                    image = R.drawable.pic2,
                )
            )
            add(
                OnboardingCarouselModel(
                    id = "3",
                    image = R.drawable.pic3,
                )
            )
        }

        onboardingAdapter.list = onboardingImageList

        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer(MarginPageTransformer(40))
        compositeTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scrollY = (0.85f + r * 0.15f).toInt()
        }
        vpOnboarding.setPageTransformer(compositeTransformer)
        vpOnboarding.adapter = onboardingAdapter

        startAutoScroll()
    }

    private fun startAutoScroll() = binding.apply{
        autoScrollJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                for (i in 0 until onboardingAdapter.itemCount) {
                        vpOnboarding.setCurrentItem(i, true)
                        delay(4000L)
                }
            }
        }
    }
}