package com.dicoding.mystoryapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.isError.observe(this) {
            if (it == false) {
                showToast(getString(R.string.success_regist))
                @Suppress("DEPRECATION")
                onBackPressed()
            }
        }

        registerViewModel.message.observe(this) {
            showToast(it)
        }
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            sendData()
        }

        playAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility =  View.GONE
            }
    }

    private fun sendData() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        if (binding.edRegisterName.length() != 0 && binding.edRegisterEmail.length() != 0 && binding.edRegisterPassword.length() >= 8) {
            registerViewModel.createAccount(name, email, password)
        } else {
            Toast.makeText(this, getString(R.string.empty_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.registerPhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.registerTitle, View.ALPHA, 1f).setDuration(800)
        val titleDesc = ObjectAnimator.ofFloat(binding.registerDesc, View.ALPHA, 1f).setDuration(800)

        val nameTV = ObjectAnimator.ofFloat(binding.nameLabel, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val nameLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val nameEdtText = ObjectAnimator.ofFloat(binding.edRegisterName, View.TRANSLATION_Y, 200f, 0f).setDuration(500)

        val nameTVAlpha = ObjectAnimator.ofFloat(binding.nameLabel, View.ALPHA, 1f).setDuration(500)
        val nameLayoutAlpha = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val nameEdtTextAlpha = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)

        val emailTV = ObjectAnimator.ofFloat(binding.emailLabel, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val emailEdtText = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.TRANSLATION_Y, 200f, 0f).setDuration(500)

        val emailTVAlpha = ObjectAnimator.ofFloat(binding.emailLabel, View.ALPHA, 1f).setDuration(500)
        val emailLayoutAlpha = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEdtTextAlpha = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)

        val passwordTV = ObjectAnimator.ofFloat(binding.passwordLabel, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val passwordEdtText = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.TRANSLATION_Y, 200f, 0f).setDuration(500)

        val passwordTVAlpha = ObjectAnimator.ofFloat(binding.passwordLabel, View.ALPHA, 1f).setDuration(500)
        val passwordLayoutAlpha = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEdtTextAlpha = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)

        val buttonRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.TRANSLATION_Y, 200f, 0f).setDuration(500)
        val buttonRegisterAlpha = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)


        val email = AnimatorSet().apply {
            playTogether(emailTV, emailLayout, emailEdtText, emailTVAlpha, emailLayoutAlpha, emailEdtTextAlpha)
        }

        val name = AnimatorSet().apply {
            playTogether(nameTV, nameLayout, nameEdtText, nameTVAlpha, nameLayoutAlpha, nameEdtTextAlpha)
        }

        val password = AnimatorSet().apply {
            playTogether(passwordTV, passwordLayout, passwordEdtText, passwordTVAlpha, passwordLayoutAlpha, passwordEdtTextAlpha)
        }

        val button = AnimatorSet().apply {
            playTogether(buttonRegister, buttonRegisterAlpha)
        }
        AnimatorSet().apply {
            playSequentially(title, titleDesc, name, email, password, button)
            start()
        }
    }

    private fun showToast (message: String?) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
    }


}