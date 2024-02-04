package com.example.shoppie.utils

import androidx.fragment.app.Fragment
import com.example.shoppie.MainActivity
import com.example.shoppie.views.MainFragment

fun Fragment.requireMainActivity(): MainActivity {
    return requireActivity() as MainActivity
}

fun Fragment.requireMainFragment(): MainFragment {
    var fragment = this
    val fragmentHierarchy = mutableListOf<String>()
    while (true) {
        if (fragment is MainFragment) {
            return fragment
        }
        fragmentHierarchy.add(fragment::class.qualifiedName ?: "Unnamed Fragment")
        fragment = fragment.parentFragment ?: break
    }
    error("Unable to find mainFragment in the hierarchy: $fragmentHierarchy}")
}