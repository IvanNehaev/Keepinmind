package com.nehaev.keepinmind.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nehaev.keepinmind.R

class LoginViewModel: ViewModel() {

    val state = MutableLiveData<LoginState>(LoginState.DefaultState)

    fun login(email: String, password: String) {
        if (!validateEmail(email = email)) {
            state.value = LoginState.ErrorState(message = R.string.error_email_invalid)
            return
        }

        if (!validatePassword(password = password)) {
            state.value = LoginState.ErrorState(message = R.string.error_password_invalid)
        }
    }

    private  fun validateEmail(email: String): Boolean {
        if (email.isBlank() || !email.contains("@")) return false
        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isEmpty() || password.length < 4) return false
        return true
    }
}