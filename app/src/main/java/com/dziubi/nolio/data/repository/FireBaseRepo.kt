package com.dziubi.nolio.data.repository

import com.dziubi.nolio.data.models.EventModel
import com.dziubi.nolio.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await


class FireBaseRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()


    fun getCurrentUser() = auth.currentUser

    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return auth.signInWithEmailAndPassword(email, password)
            .await()
            .user
    }

    suspend fun registerNewUserByEmail(email: String, password: String, name: String): FirebaseUser? {

       val firebaseUser = auth.createUserWithEmailAndPassword(email, password)
           .await()
           .user

        addNewUserToDatabase(firebaseUser, name)

        return firebaseUser
    }

    private suspend fun addNewUserToDatabase(firebaseUser: FirebaseUser?, name: String) {
        firebaseUser ?: throw Exception("User cannot be null")

        val user = UserModel(
            id = firebaseUser.uid,
            name = name,
            email = firebaseUser.email ?: ""
        )
        db.collection("users")
            .add(user)
            .await()
    }

    suspend fun getAllEvents(): List<EventModel> {
        return db.collection("events")
            .get()
            .await()
            .toObjects(EventModel::class.java)
            .toList()
    }

    suspend fun getUser(userId: String?): UserModel? {
        userId ?: return null

        return db.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .await()
            .toObjects(UserModel::class.java)
            .firstOrNull()
    }
    suspend fun getUserEvents(userId: String?) : List<EventModel>{
        userId ?: return emptyList()

        return db.collection("events")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(EventModel::class.java)
            .toList()
    }
    suspend fun uploadEventData(
        eventModel: EventModel,
    ){
//        val fileUri = Uri.fromFile(file)
//        val ref = "images/${eventModel.userId}/${fileUri.lastPathSegment}"
//        val photoRef = storage.reference
//            .child(ref)
//
//        photoRef.putFile(fileUri)
//            .await()

        insertEvent(eventModel)
    }

    private suspend fun  insertEvent(eventModel: EventModel) {

//         val downloadUrl = storage
//             .getReference(ref)
//             .downloadUrl
//             .await()
//             .toString()

        eventModel.imageUrl = ""

        db.collection("events")
            .document()
            .also {
                eventModel.apply {
                    id = it.id
                }
                it.set(eventModel)
            }
    }
    fun logout(){
        auth.signOut()
    }


}