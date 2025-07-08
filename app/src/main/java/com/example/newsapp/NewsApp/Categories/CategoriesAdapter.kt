package com.example.newsapp.NewsApp.Categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Model.Category
import com.example.newsapp.databinding.ItemCategoryBinding

class CategoriesAdapter (val items : List<Category> = Category.getCategories(),
                         val onCategoryClick :( (category:Category)->Unit)):
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(val binding:ItemCategoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.image.setImageResource(category.imageRes)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            onCategoryClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}