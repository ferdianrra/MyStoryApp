package com.dicoding.mystoryapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.mystoryapp.databinding.ActivityWelcomeBinding
import com.dicoding.mystoryapp.view.register.RegisterActivity
import com.dicoding.mystoryapp.view.signin.SigninActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnSignin.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.welcomePhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleWelcome, View.ALPHA, 1f).setDuration(800)
        val desc = ObjectAnimator.ofFloat(binding.descWelcome, View.ALPHA, 1f).setDuration(800)
        val btnSignin = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(800)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(800)

        AnimatorSet().apply {
            playSequentially(title, desc, btnSignin, btnRegister)
            start()
        }
    }

}