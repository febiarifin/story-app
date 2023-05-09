package com.febiarifin.storyappsubmissiondicoding.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.febiarifin.storyappsubmissiondicoding.R
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityMainBinding
import com.febiarifin.storyappsubmissiondicoding.ui.adapter.LoadingStateAdapter
import com.febiarifin.storyappsubmissiondicoding.ui.adapter.StoryAdapter
import com.febiarifin.storyappsubmissiondicoding.ui.addstory.upload.UploadActivity
import com.febiarifin.storyappsubmissiondicoding.ui.auth.login.LoginActivity
import com.febiarifin.storyappsubmissiondicoding.ui.detail.DetailActivity
import com.febiarifin.storyappsubmissiondicoding.ui.maps.MapsActivity
import com.febiarifin.storyappsubmissiondicoding.utils.UserPreference
import com.febiarifin.storyappsubmissiondicoding.utils.getTimeAgoFormat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)
        checkIsLogin()

        setUpRecyclerView()
        getStory()

        binding.apply {
            isRefreshing(true)

            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
                isRefreshing(true)
                getStory()
            }

            btnAddStory.buttonEnabled("+", 24f)
            btnAddStory.setOnClickListener {
                val uploadIntent = Intent(this@MainActivity, UploadActivity::class.java)
                startActivity(uploadIntent)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUpRecyclerView()
        isRefreshing(true)
        getStory()
    }

    private fun setUpRecyclerView() {
        adapter = StoryAdapter { id, name, createdAt ->
            handleMovingPage(id, name, createdAt)
        }
        adapter!!.notifyDataSetChanged()
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }

        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && adapter.itemCount < 1) {
                showSnackBar(getString(R.string.empty))
            }
        }
    }

    private fun getStory() {
        viewModel.story.observe(this, {
            adapter.submitData(lifecycle, it)
            isRefreshing(false)
        })
    }

    private fun checkIsLogin() {
        if (userPreference.getUserToken() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        val actionBar = supportActionBar
        actionBar!!.subtitle = "Hai! " + userPreference.getUserName()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                userPreference.clearPreference()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleMovingPage(id: String?, name: String?, createdAt: String?) {
        Intent(this@MainActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_ID, id)
            it.putExtra(DetailActivity.EXTRA_NAME, name)
            it.putExtra(DetailActivity.EXTRA_CREATED_AT, createdAt.getTimeAgoFormat())
            startActivity(it, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
            finish()
        }
    }

    private fun showSnackBar(message: String?) {
        Snackbar.make(
            this.window.decorView,
            message.toString(),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun isRefreshing(state: Boolean) = if (state) binding.swipeRefresh.isRefreshing = true else binding.swipeRefresh.isRefreshing = false
}