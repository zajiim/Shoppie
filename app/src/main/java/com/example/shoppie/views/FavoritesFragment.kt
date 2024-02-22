package com.example.shoppie.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.shoppie.R
import com.example.shoppie.adapters.MyCartAdapter
import com.example.shoppie.data.models.MyCartModel
import com.example.shoppie.databinding.FragmentFavoritesBinding
import com.example.shoppie.utils.requireMainActivity

class FavoritesFragment: Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val myCartAdapter = MyCartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupRv()
    }

    private fun setupRv() = binding.apply {
        val myCartList = mutableListOf<MyCartModel>().apply {
            add(
                MyCartModel(
                    itemName = "Boat Revolution",
                    itemPrice = "450",
                    itemImage = R.drawable.head_phone,
                    itemColor = "Magenta"
                )
            )

            add(
                MyCartModel(
                    itemName = "Sennheiser Revolution",
                    itemPrice = "3650",
                    itemImage = R.drawable.head_phone2,
                    itemColor = "Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Iphone 15 Pro",
                    itemPrice = "149000",
                    itemImage = R.drawable.head_phone,
                    itemColor = "Space Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Google Pixel 8 pro",
                    itemPrice = "89000",
                    itemImage = R.drawable.head_phone2,
                    itemColor = "Matte Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Boat Revolution",
                    itemPrice = "450",
                    itemImage = R.drawable.head_phone,
                    itemColor = "Magenta"
                )
            )

            add(
                MyCartModel(
                    itemName = "Sennheiser Revolution",
                    itemPrice = "3650",
                    itemImage = R.drawable.head_phone2,
                    itemColor = "Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Iphone 15 Pro",
                    itemPrice = "149000",
                    itemImage = R.drawable.head_phone,
                    itemColor = "Space Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Google Pixel 8 pro",
                    itemPrice = "89000",
                    itemImage = R.drawable.head_phone2,
                    itemColor = "Matte Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Boat Revolution",
                    itemPrice = "450",
                    itemImage = R.drawable.head_phone,
                    itemColor = "Magenta"
                )
            )

            add(
                MyCartModel(
                    itemName = "Sennheiser Revolution",
                    itemPrice = "3650",
                    itemImage = R.drawable.head_phone2,
                    itemColor = "Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Iphone 15 Pro",
                    itemPrice = "149000",
                    itemImage = R.drawable.head_phone,
                    itemColor = "Space Black"
                )
            )

            add(
                MyCartModel(
                    itemName = "Google Pixel 8 pro",
                    itemPrice = "89000",
                    itemImage = R.drawable.head_phone2,
                    itemColor = "Matte Black"
                )
            )
        }

        myCartAdapter.list = myCartList
        rvItems.adapter = myCartAdapter
    }
}