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


/**
 * Repository class for the authentication.
 * This class is used to handle the authentication of the user.
 *
 * @property context The application context
 * @property auth The Firebase authentication instance
 * @property store The Firestore database instance
 * @property userDao The user DAO
 *
 * @constructor Creates a new AuthRepository
 */
class AuthRepository(private val context: Context) {
    private val auth: FirebaseAuth = Firebase.auth
    private val store = Firebase.firestore
    private val userDao: UserDao

    init {
        val db = PlanningDatabase.getDatabase(context)
        userDao = db.userDao()
    }

    /**
     * Function to sign up a user with email and password.
     *
     * @param email The email of the user
     * @param password The password of the user
     * @return The authentication result
     */
    suspend fun signUp(email: String, password: String): AuthResult {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = SyncedUser(result.user!!.uid, Date())
        syncUser(user)
        return result
    }

    /**
     * Function to sign in a user with email and password.
     *
     * @param email The email of the user
     * @param password The password of the user
     * @return The authentication result
     */
    suspend fun signIn(email: String, password: String): AuthResult {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = SyncedUser(result.user!!.uid, Date())
        syncUser(user)
        return result
    }

    /**
     * Function to sign in a user with Google.
     *
     * @param idToken The id token of the user
     * @return The authentication result
     */
    suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val user = SyncedUser(result.user!!.uid, Date())
        syncUser(user)
        return result
    }

    /**
     * Function to sign out the user.
     * After signing out the trips are synced to Firebase.
     */
    fun signOut() {
        auth.signOut()
        Sync.syncTripsToFirebase(context = context)
    }

    /**
     * Function to update the Firebase user.
     *
     * @param displayName The display name of the user
     * @param photoUrl The photo url of the user
     */
    suspend fun updateFirebaseUser(displayName: String? = null, photoUrl: Uri? = null) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            if (!displayName.isNullOrBlank()) setDisplayName(displayName)
            if (photoUrl != null) photoUri = photoUrl
        }
        user!!.updateProfile(profileUpdates).await()

    }

    /**
     * Function to sync the user with the Room database.
     *
     * @param user The user to sync
     */
    @Transaction
    private suspend fun syncUser(user: SyncedUser) {
        val currentUser = userDao.getUser()
        // If there is no user in the Room DB, sync the data from Firebase
        if (currentUser == null) {
            Sync.syncRoomDbFromFirebase(context, user.id) // Sync data from Firebase to Room DB
            userDao.insertUser(user) // Insert the user in the Room DB
            return
        }

        // If the current user is the same as the user that is being synced, return
        if (currentUser.id == user.id) {
            return
        } else {
            Sync.syncRoomDbFromFirebase(context, user.id) // Sync data from Firebase to Room DB
            userDao.deleteUser(currentUser) // Delete the current user from the Room DB
            userDao.insertUser(user)
        }
    }

    fun deleteAccount() {
        auth.currentUser?.delete()
    }

    /**
     * Function to add a user to the Firestore database.
     */
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