package com.febiarifin.storyappsubmissiondicoding.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.ViewModelProvider
import com.febiarifin.storyappsubmissiondicoding.R
import com.febiarifin.storyappsubmissiondicoding.data.model.Login
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityLoginBinding
import com.febiarifin.storyappsubmissiondicoding.ui.auth.register.RegisterActivity
import com.febiarifin.storyappsubmissiondicoding.ui.main.MainActivity
import com.febiarifin.storyappsubmissiondicoding.utils.UserPreference
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(LoginViewModel::class.java)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        setUpView()

        binding.btnSignIn.buttonEnabled("Sign In", 14f)
        binding.btnSignIn.setOnClickListener {
            if (!isFormValid()) {
                showSnackBar(getString(R.string.form_error))
                return@setOnClickListener
            }

            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(this@LoginActivity,email, password)
        }

        viewModel.isLoading.observe(this,{
            showLoading(it)
        })

        viewModel.message.observe(this,{
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })

        viewModel.getUserLogin().observe(this,{
            saveUserLogin(it.name, it.userId, it.token)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }

    private fun setUpView() {
        binding?.apply {
            val registerText = buildSpannedString {
                append(getString(R.string.register_question)).bold {
                    append(getString(R.string.register))
                }
            }
            tvRegister.text = registerText
            tvRegister.setOnClickListener{
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }

    private fun isFormValid(): Boolean {
        binding?.apply {
            val email = edLoginEmail.isValidEmail(edLoginEmail.text)
            val password = edLoginPassword.isValidPassword(edLoginPassword.text)
            return email and password
        }
        return false
    }

    private fun showSnackBar(message: String?) {
        Snackbar.make(
            this.window.decorView,
            message.toString(),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun saveUserLogin(name: String?, userId: String?, token: String?){
        val userPreference = UserPreference(this)
        val userLogin = Login()

        userLogin.name = name
        userLogin.userId = userId
        userLogin.token = token

        userPreference.setUser(userLogin)
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility = View.VISIBLE else binding.progressbar.visibility = View.GONE
}