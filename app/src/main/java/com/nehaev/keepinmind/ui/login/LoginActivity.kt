package com.nehaev.keepinmind.ui.login

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.nehaev.keepinmind.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
    }
}