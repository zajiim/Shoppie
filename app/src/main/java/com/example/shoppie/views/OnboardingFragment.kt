package com.example.shoppie.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
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
//    private var fadeJob: Job? = null

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

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScroll()
//        fadeJob?.cancel()
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
        setupNavigation()
    }

    private fun setupNavigation() = binding.apply{
        btnCreateAccount.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToCreateAccountFragment())
        }

        btnAlreadyHaveAc.setOnClickListener {  }

    }

    private fun setUpViewPager() = binding.apply {

        val onboardingImageList = mutableListOf<OnboardingCarouselModel>().apply {
            add(
                OnboardingCarouselModel(
                    id = "1",
                    image = R.drawable.pic1,
                    title = "Various Collections Of The Latest Products",
                    subtitle = "Lorem ipsum lorem ipsum valorant vikings"
                )
            )
            add(
                OnboardingCarouselModel(
                    id = "2",
                    image = R.drawable.pic2,
                    title = "Complete Collection Of Color And Sizes",
                    subtitle = "Lorem ipsum lorem ipsum valorant vikings"
                )
            )
            add(
                OnboardingCarouselModel(
                    id = "3",
                    image = R.drawable.pic3,
                    title = "Find The Most Suitable Outfit For You",
                    subtitle = "Lorem ipsum lorem ipsum valorant vikings"
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
        vpOnboarding.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        vpOnboarding.getChildAt(onboardingAdapter.itemCount)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        vpOnboarding.adapter = onboardingAdapter

        vpOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceAsColor")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                pagerIndicator.pageCount = vpOnboarding.adapter!!.itemCount
                pagerIndicator.position = position
                pagerIndicator.positionOffset = positionOffset
                pagerIndicator.invalidate()

//                animateTitleAndSubtitle(positionOffset)
            }
        })



        startAutoScroll()
    }

//    private fun animateTitleAndSubtitle(positionOffset: Float) = binding.apply {
//        val fadeDuration = 2500L
//        val fadeIn = AlphaAnimation(0f, 1f).apply {
//            duration = fadeDuration
//            fillAfter = true
//        }
//
//        val fadeOut = AlphaAnimation(1f, 0f).apply {
//            duration = fadeDuration
//            fillAfter = true
//        }
//
//        tvTitle.startAnimation(fadeIn)
//        tvSubTitle.startAnimation(fadeIn)
//
//        fadeJob?.cancel()
//
//        fadeJob = CoroutineScope(Dispatchers.Main).launch {
//            delay(fadeDuration + 100L)
//            tvTitle.startAnimation(fadeOut)
//            tvSubTitle.startAnimation(fadeOut)
//        }
//
//    }

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
    private fun stopAutoScroll() {
        autoScrollJob.cancel()
    }
}