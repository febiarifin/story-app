package com.febiarifin.storyappsubmissiondicoding.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object{
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_CREATED_AT = "extra_created_at"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        val name = intent.getStringExtra(EXTRA_NAME)
        val created_at = intent.getStringExtra(EXTRA_CREATED_AT)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        viewModel.setStoryDetail(id.toString())
        viewModel.getStoryDetail().observe(this, {
            if (it != null){
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(it.story.photoUrl)
                        .into(ivPhoto)
                    tvDescription.text = it.story.description
                }
            }
        })

        val actionBar = supportActionBar
        actionBar!!.title = name
        actionBar!!.subtitle = created_at
        actionBar.setDisplayHomeAsUpEnabled(true)

        viewModel.isLoading.observe(this,{
            showLoading(it)
        })

        viewModel.message.observe(this, {
            Toast.makeText(this , it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility = View.VISIBLE else binding.progressbar.visibility = View.GONE
}