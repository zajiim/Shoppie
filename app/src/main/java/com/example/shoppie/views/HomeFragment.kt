package com.example.shoppie.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.shoppie.data.models.BestSellersModel
import com.example.ecomapp.models.CarouselModel
import com.example.ecomapp.models.CarouselModel2
import com.example.ecomapp.models.CategoriesModel
import com.example.shoppie.data.models.FeaturedProductModel
import com.example.shoppie.adapters.BestSellersAdapter
import com.example.shoppie.adapters.CarouselAdapter
import com.example.shoppie.adapters.CategoriesAdapter
import com.example.shoppie.adapters.FeaturedProductsAdapter
import com.example.shoppie.adapters.ItemsCarousel
import com.example.shoppie.R
import com.example.shoppie.databinding.FragmentHomeBinding
import com.example.shoppie.utils.requireMainFragment
import kotlin.math.abs

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private  var carouselAdapter = CarouselAdapter()
    private  var itemCarouselAdapter = ItemsCarousel()
    private  var categoriesAdapter = CategoriesAdapter()
    private  var featuredProductsAdapter = FeaturedProductsAdapter()
    private  var bestSellersAdapter = BestSellersAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }




    private fun setupViews() {
        setViewPager()
        setCategoriesRV()
        setFeaturedProductsRV()
        setViewPager2()
        setBestSellers()
        setViewPager3()
        setNewArrival()
        setTopRatedProducts()
        setSpecialOffers()
    }

    private fun setSpecialOffers() = binding.apply{
        val specialOffersList = mutableListOf<FeaturedProductModel>().apply {
            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "1300",
                    rating = 4.9f,
                    reviewCounts = "456 Reviews",
                    productName = "JBL WoofBang"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "11000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "Bose HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "46 Reviews",
                    productName = "TMA-5 HD Wireless"
                )
            )
        }

        featuredProductsAdapter.list = specialOffersList
        rvSpecialOffers.adapter = featuredProductsAdapter
        featuredProductsAdapter.onClick = { clickedItem ->
            requireMainFragment().findNavController().navigate(MainFragmentDirections.actionMainFragmentToProductDetailsFragment(clickedItem))

        }
    }
    private fun setTopRatedProducts() = binding.apply{
        val topRatedProductsList = mutableListOf<FeaturedProductModel>().apply {
            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "1300",
                    rating = 4.9f,
                    reviewCounts = "456 Reviews",
                    productName = "JBL WoofBang"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "11000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "Bose HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "46 Reviews",
                    productName = "TMA-5 HD Wireless"
                )
            )
        }

        featuredProductsAdapter.list = topRatedProductsList
        rvTopRated.adapter = featuredProductsAdapter
    }
    private fun setNewArrival() = binding.apply{
        val newArrivalsList = mutableListOf<FeaturedProductModel>().apply {
            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "1300",
                    rating = 4.9f,
                    reviewCounts = "456 Reviews",
                    productName = "JBL WoofBang"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "11000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "Bose HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "46 Reviews",
                    productName = "TMA-5 HD Wireless"
                )
            )
        }

        featuredProductsAdapter.list = newArrivalsList
        rvNewArrivals.adapter = featuredProductsAdapter
    }

    private fun setViewPager3() = binding.apply{
        val carouselList3 = mutableListOf<CarouselModel2>().apply {
            add(
                CarouselModel2(
                    id = "1",
                    image = R.drawable.head_phone2,
                    title = "Co2 - Cable Multifunction",
                    color = R.color.primary_violet
                )
            )
            add(
                CarouselModel2(
                    id = "2",
                    image = R.drawable.head_phone,
                    title = "boAt Revolution",
                    color = R.color.green
                ),
            )

            add(
                CarouselModel2(
                    id = "2",
                    image = R.drawable.head_phone,
                    title = "boAt Revolution",
                    color = R.color.red
                ),
            )
            add(
                CarouselModel2(
                    id = "1",
                    image = R.drawable.head_phone2,
                    title = "Co2 - Cable Multifunction",
                    color = R.color.red
                )
            )
        }

        itemCarouselAdapter.list = carouselList3

        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer(MarginPageTransformer(40))
        compositeTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scrollY = (0.85f + r * 0.15f).toInt()
        }
        vpCarousel3.setPageTransformer(compositeTransformer)
        vpCarousel3.adapter = itemCarouselAdapter
    }

    private fun setBestSellers() = binding.apply{
        val bestSellersList = mutableListOf<BestSellersModel>().apply {
            add(
                BestSellersModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )

            add(
                BestSellersModel(
                    image = R.drawable.head_phone2,
                    price = "1300",
                    rating = 4.9f,
                    reviewCounts = "456 Reviews",
                    productName = "JBL WoofBang"
                )
            )


            add(
                BestSellersModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )


            add(
                BestSellersModel(
                    image = R.drawable.head_phone,
                    price = "11000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "Bose HD Wireless"
                )
            )

            add(
                BestSellersModel(
                    image = R.drawable.head_phone2,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "46 Reviews",
                    productName = "TMA-5 HD Wireless"
                )
            )
        }

        bestSellersAdapter.list = bestSellersList
        rvBestSellers.adapter = bestSellersAdapter
    }

    private fun setViewPager2() = binding.apply{
        val carouselList2 = mutableListOf<CarouselModel2>().apply {
            add(
                CarouselModel2(
                    id = "1",
                    image = R.drawable.head_phone2,
                    title = "Co2 - Cable Multifunction",
                    color = R.color.primary_violet
                )
            )
            add(
                CarouselModel2(
                    id = "2",
                    image = R.drawable.head_phone,
                    title = "boAt Revolution",
                    color = R.color.green
                ),
            )
        }

        itemCarouselAdapter.list = carouselList2

        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer(MarginPageTransformer(40))
        compositeTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scrollY = (0.85f + r * 0.15f).toInt()
        }
        vpCarousel2.setPageTransformer(compositeTransformer)
        vpCarousel2.adapter = itemCarouselAdapter
    }


    private fun setFeaturedProductsRV() = binding.apply{
        val featuredProductsList = mutableListOf<FeaturedProductModel>().apply {
            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "1300",
                    rating = 4.9f,
                    reviewCounts = "456 Reviews",
                    productName = "JBL WoofBang"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "TMA-2 HD Wireless"
                )
            )


            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone,
                    price = "11000",
                    rating = 4.6f,
                    reviewCounts = "86 Reviews",
                    productName = "Bose HD Wireless"
                )
            )

            add(
                FeaturedProductModel(
                    image = R.drawable.head_phone2,
                    price = "15000",
                    rating = 4.6f,
                    reviewCounts = "46 Reviews",
                    productName = "TMA-5 HD Wireless"
                )
            )
        }

        featuredProductsAdapter.list = featuredProductsList
        rvFeaturedProducts.adapter = featuredProductsAdapter
    }

    private fun setCategoriesRV() = binding.apply {
        val categoriesList = mutableListOf<CategoriesModel>().apply {
            add(
                CategoriesModel(
                    title = "Foods",
                    icon = R.drawable.ic_food_rv
                )
            )
            add(
                CategoriesModel(
                    title = "Gifts",
                    icon = R.drawable.ic_gifts_rv
                )
            )
            add(
                CategoriesModel(
                    title = "Fashion",
                    icon = R.drawable.ic_fashion_rv
                )
            )
            add(
                CategoriesModel(
                    title = "Gadgets",
                    icon = R.drawable.ic_gadget_rv
                )
            )
            add(
                CategoriesModel(
                    title = "Computer",
                    icon = R.drawable.ic_computer_rv
                )
            )
            add(
                CategoriesModel(
                    title = "Fashion",
                    icon = R.drawable.ic_fashion_rv
                )
            )
        }
        categoriesAdapter.list = categoriesList
        rvCategories.adapter = categoriesAdapter
    }

    private fun setViewPager() = binding.apply{
        val carouselList = mutableListOf<CarouselModel>().apply {
            add(
                CarouselModel(
                    id = "1",
                    image = R.drawable.carousel1,
                    drawable = R.drawable.carousel_foreground,
                    title = "Summer sale is here",
                    subtitle = "From April 13 onwards"
                )
            )
            add(
                CarouselModel(
                    id = "1",
                    image = R.drawable.carousel1,
                    drawable = R.drawable.carousel_foreground,
                    title = "Winter sale is here",
                    subtitle = "From September 18 onwards"
                ),
            )
        }
        carouselAdapter.list = carouselList

        val compositeTransformer = CompositePageTransformer()
        compositeTransformer.addTransformer(MarginPageTransformer(40))
        compositeTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scrollY = (0.85f + r * 0.15f).toInt()
        }
        vpCarousel.setPageTransformer(compositeTransformer)
        vpCarousel.adapter = carouselAdapter
    }

}