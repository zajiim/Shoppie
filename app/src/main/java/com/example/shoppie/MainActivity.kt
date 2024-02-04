package com.example.shoppie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppie.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val _windowsInsetsMutableLiveData = MutableLiveData<WindowInsetsCompat>()
    val windowsInsetsLiveData: LiveData<WindowInsetsCompat>
        get() = _windowsInsetsMutableLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { _, insets ->
            _windowsInsetsMutableLiveData.postValue(insets)
            return@setOnApplyWindowInsetsListener WindowInsetsCompat.CONSUMED
        }
    }
}