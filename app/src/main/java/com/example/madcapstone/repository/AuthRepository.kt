package com.example.madcapstone.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val store = Firebase.firestore

    suspend fun signUp(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential).await()
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateFirebaseUser(displayName: String? = null, photoUrl: Uri? = null) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
               if (!displayName.isNullOrBlank()) setDisplayName(displayName)
                if (photoUrl != null) photoUri = photoUrl
        }
        user!!.updateProfile(profileUpdates).await()

    }

    fun deleteAccount() {
        auth.currentUser?.delete()
    }

    suspend fun addUserToFirestore(uuid: String, email: String, name: String, photoUrl: Uri? = null) {
        val user = hashMapOf(
            "email" to email,
            "name" to name,
        )

        photoUrl?.let {
            user["photoUrl"] = it.toString()
        }

        store.collection("users")
            .document(uuid)
            .set(user).await()
    }
}