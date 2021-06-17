package com.nehaev.keepinmind.ui.login

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nehaev.keepinmind.R

class LoginActivity : AppCompatActivity() {

    lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        mViewModel = viewModelInit()
    }

    private fun viewModelInit(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }
}