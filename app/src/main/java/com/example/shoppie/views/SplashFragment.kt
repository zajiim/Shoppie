package com.example.shoppie.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoppie.databinding.FragmentSplashBinding
import com.example.shoppie.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            delay(3000L)
            viewModel.isAuthUser.collect { user ->
                if (user) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
                } else {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
                }
            }
        }
        return binding.root
    }
}