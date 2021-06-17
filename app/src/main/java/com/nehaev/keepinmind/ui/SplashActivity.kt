package com.nehaev.keepinmind.ui

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nehaev.keepinmind.R
import com.nehaev.keepinmind.ui.tests.TestsActivity

class SplashActivity : AppCompatActivity() {

    private companion object {
        const val SPLASH_DURATION = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        backgroundAnimation()

        val intent = Intent(this, TestsActivity::class.java)

        Handler(Looper.getMainLooper()).postDelayed( {
            startActivity( intent)
            finish()
        }, SPLASH_DURATION.toLong())
    }

    private fun backgroundAnimation() {

        // Background color animation

        ObjectAnimator.ofObject(findViewById<FrameLayout>(R.id.flSplash),
        "backgroundColor",
        ArgbEvaluator(),
        ContextCompat.getColor(this, R.color.splash_color_from),
        ContextCompat.getColor(this, R.color.splash_color_to)).apply {
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            duration = SPLASH_DURATION.toLong()
        }.start()

        // Logo alpha animation

        val mImageView = findViewById<ImageView>(R.id.ivLogoSplash)
        val animationX = ObjectAnimator.ofFloat(mImageView, "alpha", 0F)
        val animationY = ObjectAnimator.ofFloat(mImageView, "alpha", 1F)
        val set = AnimatorSet()
        set.play(animationX)
                .with(animationY)
        set.duration = SPLASH_DURATION.toLong()
        set.interpolator = DecelerateInterpolator()
        set.start()
    }
}