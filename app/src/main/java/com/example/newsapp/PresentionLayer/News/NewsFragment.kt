package com.example.newsapp.PresentionLayer.News

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.DataLayer.Api.Model.Category
import com.example.newsapp.DataLayer.Api.Model.SourcesResponse.Source
import com.example.newsapp.PresentionLayer.ViewModels.NewsViewModel
import com.example.newsapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private val viewModel: NewsViewModel by viewModels()
    private val adapter = NewsAdapter()

    private lateinit var category: Category

    companion object {
        private const val CATEGORY_KEY = "category"

        fun getInstance(category: Category): NewsFragment {
            val fragment = NewsFragment()
            val bundle = Bundle()
            bundle.putParcelable(CATEGORY_KEY, category)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = requireArguments().getParcelable(CATEGORY_KEY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        viewModel.loadSources(category.id)
    }

    private fun initViews() {
        binding.newsRecycler.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingView.isVisible = isLoading
            binding.errorView.isVisible = false
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                showErrorView(errorMsg) {
                    viewModel.clearError()
                    viewModel.loadSources(category.id)
                }
            }
        }

        viewModel.sources.observe(viewLifecycleOwner) { sources ->
            Log.d("NewsFragment", "Loaded ${sources.size} sources")
            bindTabLayout(sources)
        }

        viewModel.newsList.observe(viewLifecycleOwner) { news ->
            Log.d("NewsFragment", "Received ${news.size} news items")
            adapter.changeData(news)
        }
    }

    private fun bindTabLayout(sources: List<Source>) {
        binding.sourcesTabs.removeAllTabs()

        sources.forEach { source ->
            val tab = binding.sourcesTabs.newTab()
            tab.text = source.name
            tab.tag = source
            binding.sourcesTabs.addTab(tab)
        }

        binding.sourcesTabs.addOnTabSelectedListener(
            object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                    val source = tab?.tag as? Source
                    source?.id?.let {
                        Log.d("NewsFragment", "Selected source: $it")
                        viewModel.loadNews(it)
                    }
                }

                override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
                override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                    val source = tab?.tag as? Source
                    source?.id?.let {
                        Log.d("NewsFragment", "Reselected source: $it")
                        viewModel.loadNews(it)
                    }
                }
            }
        )

        // Select first tab by default
        if (binding.sourcesTabs.tabCount > 0) {
            binding.sourcesTabs.getTabAt(0)?.select()
        }
    }

    private fun showErrorView(errorMessage: String, onTryAgainClick: () -> Unit) {
        binding.loadingView.isVisible = false
        binding.errorView.isVisible = true
        binding.errorMessage.text = errorMessage
        binding.tryAgainBtn.setOnClickListener { onTryAgainClick() }
    }
}
