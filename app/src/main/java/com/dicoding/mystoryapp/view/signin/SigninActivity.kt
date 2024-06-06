package com.dicoding.mystoryapp.view.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.network.pref.UserModel
import com.dicoding.mystoryapp.databinding.ActivitySigninBinding
import com.dicoding.mystoryapp.view.ViewModelFactory
import com.dicoding.mystoryapp.view.main.MainActivity

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private val viewModel by viewModels<SigninViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.resultAccount.observe(this){
            viewModel.saveSession(UserModel(it.token))
            setupAction(it.token)
        }

        binding.signinBtn.setOnClickListener {
            val email =binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                showToast(getString(R.string.empty_error))
            } else {
                viewModel.loginAccount(email, password)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.message.observe(this) {
            showToast(it)
        }

        playAnimation()
    }

    private fun showLoading(isloading: Boolean) {
        binding.progressBar.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.signinPhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.welcomeTitle, View.ALPHA, 1f).setDuration(800)
        val titleDesc = ObjectAnimator.ofFloat(binding.welcomeDesc, View.ALPHA, 1f).setDuration(800)

        val emailTV = ObjectAnimator.ofFloat(binding.emailLabel, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val emailEdtText = ObjectAnimator.ofFloat(binding.edLoginEmail, View.TRANSLATION_Y, 200f, 0f).setDuration(500)

        val emailTVAlpha = ObjectAnimator.ofFloat(binding.emailLabel, View.ALPHA, 1f).setDuration(500)
        val emailLayoutAlpha = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEdtTextAlpha = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)

        val passwordTV = ObjectAnimator.ofFloat(binding.passwordLabel, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val passwordEdtText = ObjectAnimator.ofFloat(binding.edLoginPassword, View.TRANSLATION_Y, 200f, 0f).setDuration(500)

        val passwordTVAlpha = ObjectAnimator.ofFloat(binding.passwordLabel, View.ALPHA, 1f).setDuration(500)
        val passwordLayoutAlpha = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEdtTextAlpha = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)

        val buttonSignin = ObjectAnimator.ofFloat(binding.signinBtn, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val buttonSigninAlpha = ObjectAnimator.ofFloat(binding.signinBtn, View.ALPHA, 1f).setDuration(500)


        val email = AnimatorSet().apply {
            playTogether(emailTV, emailLayout, emailEdtText, emailTVAlpha, emailLayoutAlpha, emailEdtTextAlpha)
        }

        val password = AnimatorSet().apply {
            playTogether(passwordTV, passwordLayout, passwordEdtText, passwordTVAlpha, passwordLayoutAlpha, passwordEdtTextAlpha)
        }

        val button = AnimatorSet().apply {
            playTogether(buttonSignin, buttonSigninAlpha)
        }
        AnimatorSet().apply {
            playSequentially(title, titleDesc, email, password, button)
            start()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupAction(token: String?) {
        if (token != null) {
            showToast(getString(R.string.success_login))
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }
}