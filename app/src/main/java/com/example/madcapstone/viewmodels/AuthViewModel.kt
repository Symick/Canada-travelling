package com.example.madcapstone.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val authRepository = AuthRepository()
    fun isValidateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotBlank()
    }

    fun isValidatePassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidateName(name: String): Boolean {
        return name.isNotBlank()
    }

    fun isPasswordMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun signUp(email: String, password: String, name: String): LiveData<Resource<Boolean>> {
        if (isValidateEmail(email) && isValidatePassword(password) && isValidateName(name) && isPasswordMatch(password, password)) {
            viewModelScope.launch {
                authRepository.signUp(email, password)
            }

        }
    }

    fun signIn(email: String, password: String) {
    }

    fun signOut() {
    }

    fun deleteAccount() {
    }


}
