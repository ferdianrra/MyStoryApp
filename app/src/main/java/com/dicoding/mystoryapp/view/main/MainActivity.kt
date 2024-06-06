package com.dicoding.mystoryapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.network.response.ListStoryItem
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.di.Injection
import com.dicoding.mystoryapp.view.AddStory.AddStoryActivity
import com.dicoding.mystoryapp.view.ViewModelFactory
import com.dicoding.mystoryapp.view.detail.DetailFeedsActivity
import com.dicoding.mystoryapp.view.maps.MapsActivity
import com.dicoding.mystoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var token : String
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val layoutManager =LinearLayoutManager(this)
        binding.feedsUserRv.layoutManager =layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.feedsUserRv.addItemDecoration(itemDecoration)


        viewModel.getSession().observe(this) { user ->
            if (user.token == null) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                token = user.token
                viewModel.updateToken(token)
                showFeedsData(token)
            }

            viewModel.isLoading.observe(this) {
                showLoading(it)
            }


            binding.apply {
                refreshFeeds.setOnRefreshListener {
                    refreshFeeds.isRefreshing = false
                    viewModel.refreshFeeds()
                }
                AppBarHome.setOnMenuItemClickListener { menu ->
                    when(menu.itemId) {
                        R.id.action_logout -> {
                            viewModel.logout()
                            true
                        }
                        R.id.maps -> {
                            val mapsIntent = Intent(this@MainActivity, MapsActivity::class.java)
                            mapsIntent.putExtra("TOKEN", user.token)
                            startActivity(mapsIntent)
                            true
                        }
                        else -> {
                            false
                        }
                    }

                }

                postPhoto.setOnClickListener {
                    val intentPost = Intent(this@MainActivity, AddStoryActivity::class.java)
                    intentPost.putExtra("TOKENS", user.token)
                    startActivity(intentPost)
                }
            }
        }

    }

    private fun showLoading(isloading: Boolean) {
        binding.progressBar.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    private fun showFeedsData(token:String) {
        val adapter =ReviewFeedsAdapter()
        binding.feedsUserRv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.feedsPhoto.observe(this, {
            adapter.submitData(lifecycle, it)
        })

        adapter.setOnItemClickCallback(object : ReviewFeedsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                val intentDetail =Intent(this@MainActivity, DetailFeedsActivity::class.java)
                intentDetail.putExtra("ID", data.id)
                intentDetail.putExtra("TOKEN", token)
                startActivity(intentDetail)
            }
        })
    }

}