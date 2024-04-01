package com.example.madcapstone.repository

import android.content.Context
import android.net.Uri
import androidx.room.Transaction
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.SyncedUser
import com.example.madcapstone.utils.Sync
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date


class AuthRepository(private val context: Context) {
    private val auth: FirebaseAuth = Firebase.auth
    private val store = Firebase.firestore
    private val userDao: UserDao

    init {
        val db = PlanningDatabase.getDatabase(context)
        userDao = db.userDao()
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = SyncedUser(result.user!!.uid, Date())
        syncUser(user)
        return result
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = SyncedUser(result.user!!.uid, Date())
        syncUser(user)
        return result
    }

    suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val user = SyncedUser(result.user!!.uid, Date())
        syncUser(user)
        return result
    }

    fun signOut() {
        auth.signOut()
        Sync.syncTripsToFirebase(context = context)
    }

    suspend fun updateFirebaseUser(displayName: String? = null, photoUrl: Uri? = null) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            if (!displayName.isNullOrBlank()) setDisplayName(displayName)
            if (photoUrl != null) photoUri = photoUrl
        }
        user!!.updateProfile(profileUpdates).await()

    }

    @Transaction
    private suspend fun syncUser(user: SyncedUser) {
        val currentUser = userDao.getUser()
        if (currentUser == null) {
            userDao.insertUser(user)
            return
        }

        if (currentUser.id == user.id) {
            return
        } else {
            userDao.deleteUser(currentUser)
            userDao.insertUser(user)
            // todo handle firestore to room sync
        }
    }

    fun deleteAccount() {
        auth.currentUser?.delete()
    }

    suspend fun addUserToFirestore(
        uuid: String,
        email: String,
        name: String,
        photoUrl: Uri? = null
    ) {
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