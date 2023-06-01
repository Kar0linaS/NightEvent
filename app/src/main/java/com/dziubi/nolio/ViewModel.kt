package com.dziubi.nolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dziubi.nolio.data.models.EventModel
import com.dziubi.nolio.data.models.LocationModel
import com.dziubi.nolio.data.models.UserModel
import com.dziubi.nolio.data.repository.FireBaseRepo
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

class RegistrationViewModel : ViewModel() {

    val repo = FireBaseRepo()

    private val _firebaseUser = MutableStateFlow(getCurrentUser())
    val firebaseUser = _firebaseUser.asStateFlow()

    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    private val _allEvents = MutableStateFlow<List<EventModel>>(emptyList())
    val allEvents: StateFlow<List<EventModel>> = _allEvents.asStateFlow()

    private fun getCurrentUser(): FirebaseUser? = repo.getCurrentUser()


    fun registerNewUserByEmail(email: String, password: String, name: String) {
        viewModelScope.launch {
            val newUser = repo.registerNewUserByEmail(email, password, name)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val firebaseUser = repo.loginUser(email, password)
            _firebaseUser.update { firebaseUser }
            _user.update { repo.getUser(firebaseUser!!.uid) }
        }
    }

    fun loadAllEvents() {
        viewModelScope.launch {
            _allEvents.update {
                repo.getAllEvents()
            }
        }
    }


    fun getUserEvents(userId: String?): Flow<List<EventModel>> {
        return flow { emit(repo.getUserEvents(userId)) }
    }

    fun insertEvent(eventDesc: String) {
        viewModelScope.launch {

            repo.uploadEventData(
                EventModel(
                    desc = eventDesc,
                    location = LocationModel(
                        lat = 52.0 + Random.nextDouble(-3.0, 3.0),
                        lng = 20.0 + Random.nextDouble(-3.0, 3.0)
                    ),
                    userId = getCurrentUser()!!.uid
                )
            )
        }
    }

    fun logout(){
        repo.logout()
    }

}