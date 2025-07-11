package com.example.newsapp.PresentionLayer.Categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.DataLayer.Api.Model.Category
import com.example.newsapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    companion object{
        fun getInstance(
            onCategoryClickCallBack: OnCategoryClickCallBack
        ):CategoriesFragment {
            val fragment = CategoriesFragment()
            fragment.onCategoryClickCallBack = onCategoryClickCallBack
            return fragment
        }
    }

    fun interface OnCategoryClickCallBack {
        fun onCategoryClick(category: Category)
    }

    private var onCategoryClickCallBack :OnCategoryClickCallBack? = null

    lateinit var binding: FragmentCategoriesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(
            inflater,container,false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }
    fun initRecyclerView(){
        binding.categoriesRecycler.adapter = adapter
    }
    val adapter = CategoriesAdapter(
        onCategoryClick = :: onCategoryClick
    )
    private fun onCategoryClick(category: Category){
        onCategoryClickCallBack?.onCategoryClick(category)
    }
}
