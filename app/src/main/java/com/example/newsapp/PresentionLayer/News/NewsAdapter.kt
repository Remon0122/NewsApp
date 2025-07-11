package com.example.newsapp.PresentionLayer.News

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.DataLayer.Api.Model.newsResponse.News
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemNewsBinding
import com.github.marlonlom.utilities.timeago.TimeAgo

class NewsAdapter (var newsList : List<News?>? = null):RecyclerView.Adapter<NewsAdapter.ViewHolder>() {


    class ViewHolder(val binding:ItemNewsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(news: News?){
            binding.title.text = news?.title
            binding.date.text = news?.publishedAt
            binding.author.text = news?.author
            Glide.with(binding.root)
                .load(news?.urlToImage)
                .error(R.drawable.ic_launcher_background)
                .into(binding.image)

            val formattedDate = news?.getPublishedAtInMillis()?.let { TimeAgo.using(it) }
            binding.date.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = newsList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList?.get(position))
    }
    @SuppressLint("NotifyDataSetChanged")
    fun changeData(newList : List<News?>?){
        this.newsList = newList
        notifyDataSetChanged()
    }
}