package com.febiarifin.storyappsubmissiondicoding.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febiarifin.storyappsubmissiondicoding.R
import com.febiarifin.storyappsubmissiondicoding.data.model.Story
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityMainBinding
import com.febiarifin.storyappsubmissiondicoding.ui.adapter.StoryAdapter
import com.febiarifin.storyappsubmissiondicoding.ui.addstory.upload.UploadActivity
import com.febiarifin.storyappsubmissiondicoding.ui.auth.login.LoginActivity
import com.febiarifin.storyappsubmissiondicoding.ui.detail.DetailActivity
import com.febiarifin.storyappsubmissiondicoding.ui.maps.MapsActivity
import com.febiarifin.storyappsubmissiondicoding.utils.UserPreference
import com.febiarifin.storyappsubmissiondicoding.utils.getTimeAgoFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)
        checkIsLogin()

        adapter = StoryAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.setStories(this)
        }

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }

        binding.btnAddStory.buttonEnabled("+", 24f)
        binding.btnAddStory.setOnClickListener{
            val uploadIntent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(uploadIntent)
            finish()
        }

        viewModel.setStories(this)

        viewModel.getStories().observe(this,{
            if (it != null){
                adapter.setList(it)
            }
        })

        viewModel.isLoading.observe(this,{
            binding.swipeRefresh.isRefreshing = it
        })

        viewModel.message.observe(this,{
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Story) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_NAME, data.name)
                    it.putExtra(DetailActivity.EXTRA_CREATED_AT, data.createdAt.getTimeAgoFormat())
                    startActivity(it, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
                }
            }
        })
    }

    private fun checkIsLogin() {
        if (userPreference.getUserToken() == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        val actionBar= supportActionBar
        actionBar!!.subtitle = "Hai! "+ userPreference.getUserName()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                userPreference.clearPreference()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStories(this)
    }
}