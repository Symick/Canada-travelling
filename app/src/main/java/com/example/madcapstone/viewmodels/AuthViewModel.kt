package com.example.madcapstone.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcapstone.data.util.Resource
import com.example.madcapstone.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val authRepository = AuthRepository()

    private val _authState = MutableLiveData<Resource<Boolean>>(Resource.Empty())
    val authState: LiveData<Resource<Boolean>> get() = _authState

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

    fun signUp(email: String, password: String, name: String) {

        viewModelScope.launch {
            _authState.value = Resource.Loading()
            try {
                val authResult = authRepository.signUp(email, password)
                _authState.value = Resource.Success(true)

                val user = authResult.user
                user?.let {
                    authRepository.addUserToFirestore(it.uid, email, name)
                }

            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "An unknown error occurred while signing up.")
            }
        }

    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            try {
                val authResult = authRepository.signIn(email, password)
                _authState.value = Resource.Success(authResult != null)
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "An unknown error occurred while signing in.")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            try {
                val authResult = authRepository.signInWithGoogle(idToken)

                val newUser = authResult.additionalUserInfo?.isNewUser

                if (newUser == true) {
                    val user = authResult.user
                    user?.let {
                        authRepository.addUserToFirestore(it.uid, it.email ?: "", it.displayName ?: "", it.photoUrl)
                    }
                }

                _authState.value = Resource.Success(true)
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.localizedMessage ?: "An unknown error occurred while signing in with Google.")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun deleteAccount() {
    }

    fun resetState() {
        _authState.value = Resource.Empty()
    }

}
