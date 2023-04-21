package com.febiarifin.storyappsubmissiondicoding.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.ViewModelProvider
import com.febiarifin.storyappsubmissiondicoding.R
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityRegisterBinding
import com.febiarifin.storyappsubmissiondicoding.ui.auth.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.hide()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RegisterViewModel::class.java)

        setUpView()

        binding.btnRegister.buttonEnabled("Sign Up", 14f)
        binding.btnRegister.setOnClickListener {
            if (!isFormValid()) {
                showSnackBar(getString(R.string.form_error))
                return@setOnClickListener
            }

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.register(this@RegisterActivity, name, email, password)
        }

        viewModel.isSuccess.observe(this,{
            if (it){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewModel.message.observe(this, {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpView() {
        binding?.apply {
            val signInText = buildSpannedString {
                append(getString(R.string.sign_in_question)).bold {
                    append(getString(R.string.sign_in))
                }
            }
            tvSignIn.text = signInText
            tvSignIn.setOnClickListener{
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun isFormValid(): Boolean {
        binding?.apply {
            val name = edRegisterName.isValidName(edRegisterName.text)
            val email = edRegisterEmail.isValidEmail(edRegisterEmail.text)
            val password = edRegisterPassword.isValidPassword(edRegisterPassword.text)
            val confirmationPassword = edRegisterPasswordConfirm.isValidPassword(edRegisterPasswordConfirm.text)
            return name and email and password and confirmationPassword and edRegisterPasswordConfirm.error.isNullOrEmpty()
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


    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility = View.VISIBLE else binding.progressbar.visibility = View.GONE

}