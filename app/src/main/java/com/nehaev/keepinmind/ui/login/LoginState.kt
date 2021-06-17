package com.nehaev.keepinmind.ui.login

sealed class LoginState {
    object DefaultState: LoginState()
    class SendingState: LoginState()
    class LoginSucceededState: LoginState()
    class ErrorState<T>(val message: T): LoginState()
}
