package com.dziubi.nolio

import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.dziubi.nolio.data.models.EventModel
import com.dziubi.nolio.data.models.UserModel
import com.dziubi.nolio.screen.*
import com.dziubi.nolio.services.LocalizationBackgroundService
import com.dziubi.nolio.ui.theme.NolioTheme
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAllEvents()

        setContent {
            NolioTheme {
                val navController = rememberNavController()
                val vm = viewModel<RegistrationViewModel>()

                NavigationComponent(navController = navController, vm = vm)
            }
        }
        val intent = Intent(applicationContext, LocalizationBackgroundService::class.java)
        startService(intent)
    }
}

@Composable
fun NavigationComponent(
    navController: NavHostController,
    vm: RegistrationViewModel,
) {
    val loginScope = rememberCoroutineScope()
    val registrationScope = rememberCoroutineScope()
    val loadEvent = rememberCoroutineScope()


    NavHost(
        navController = navController,
        startDestination = "start"
    ) {

        composable(route = "start") {
            StartScreen(onLoginClick = { navController.navigate("Login") },
                onClick = { navController.navigate("rejestracja") },
                onRegistrationClick = { navController.navigate(route = "rejestracja") })
        }
        composable(route = "login") {
            LoginScreen(onClickLogin = { email, password ->
                loginScope.launch {
                    vm.loginUser(email, password)
                    navController.navigate(route = "map")
                }
            })
        }
        composable(route = "rejestracja") {
            RegistrationScreen(onRegistrationClick = { email, password, nickname ->
                registrationScope.launch {
                    vm.registerNewUserByEmail(email, password, nickname)
                    navController.navigate(route = "profile")
                }
            })
        }
        composable(route = "profile") {
            val user by vm.user.collectAsState()
            val listEvent by vm.getUserEvents(user!!.id).collectAsState(emptyList())

            ProfileScreen(
                data = user ?: return@composable,
                events = listEvent,
                onCloseClick = {
                    vm.logout()
                    navController.navigate(route = "start")
                }
            )
        }
        composable(route = "map") {
            MapScreen(viewModel = vm,
                onAddEventClick = { navController.navigate(route = "add_event") },
                onHomeClick = { navController.navigate(route = "profile") },
                onListClick = {
                    loadEvent.launch {
                        vm.loadAllEvents()
                        navController.navigate(route = "list_event")
                    }
                })
        }
        composable(route = "add_event") {

            AddEventScreen(AddEventCLick = {
                vm.insertEvent(eventDesc = it)
                navController.navigate("list_event", navOptions = navOptions {
                    popUpTo("map") {
                        inclusive = true
                    }
                })
            })
        }
        composable(route = "list_event") {
            val listEvent by vm.allEvents.collectAsState()

            ListEventsScreen(events = listEvent)
        }
    }
}


