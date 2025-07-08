package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.newsapp.Model.Category
import com.example.newsapp.NewsApp.Categories.CategoriesFragment
import com.example.newsapp.NewsApp.News.NewsFragment
import com.example.newsapp.NewsApp.Settings.SettingsFragment
import com.example.newsapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        showMainFragment(CategoriesFragment.getInstance(
            onCategoryClickCallBack = ::onCategoryClick
        ))
        setSupportActionBar(viewBinding.appBarActivityHome.toolbar)
        viewBinding.appBarActivityHome.toolbar.setNavigationOnClickListener {
            viewBinding.drawerLayout.open()
        }
        viewBinding.navView.setNavigationItemSelectedListener {item->
            when (item.itemId) {
                R.id.nav_view -> {
                    showMainFragment(CategoriesFragment.getInstance(
                        onCategoryClickCallBack = ::onCategoryClick
                    ))
                }
                R.id.settings -> {
                    ShowFragment(SettingsFragment())
                }
                else ->{}

            }
            viewBinding.drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }


    private fun onCategoryClick(category: Category){
        showNewsFragment(category)
    }
    private fun ShowFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showNewsFragment(category: Category) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,NewsFragment.getInstance(category))
            .addToBackStack(null)
            .commit()
    }

    private fun showMainFragment(fragment: CategoriesFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()

    }
}