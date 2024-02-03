package com.example.shoppie.utils

import androidx.fragment.app.Fragment
import com.example.shoppie.MainActivity

fun Fragment.requireMainActivity(): MainActivity {
    return requireActivity() as MainActivity
}