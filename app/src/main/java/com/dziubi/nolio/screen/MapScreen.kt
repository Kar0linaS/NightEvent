package com.dziubi.nolio.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dziubi.nolio.R
import com.dziubi.nolio.RegistrationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@Composable
fun MapScreen(
    viewModel: RegistrationViewModel,
    onHomeClick: () -> Unit = {},
    onAddEventClick:() -> Unit =  {},
    onListClick: () -> Unit = {}

) {
    val eventList by viewModel.allEvents.collectAsState()

    if (eventList.isEmpty()) return

    val ourLocations = eventList
        .mapNotNull { it.location }
        .map { LatLng(it.lat ?: 0.0, it.lng ?: 0.0) }

    val firebaseLatLng = LatLng(52.0, 18.0)

    var uiSettings by remember { mutableStateOf(MapUiSettings()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(firebaseLatLng, 10f)
    }

    Column() {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            ourLocations.forEach {
                Marker(
                    state = MarkerState(position = it),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }
        }
        Column(verticalArrangement = Arrangement.Bottom) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val homeImg = ImageBitmap.imageResource(id = R.drawable.home_img)
                val addImg = ImageVector.vectorResource(id = R.drawable.ic_add)
                val listImg = ImageBitmap.imageResource(id = R.drawable.list_img)
                Box() {
                    Image(
                        modifier = Modifier
                            .size(60.dp, 60.dp)
                            .padding(start = 30.dp)
                            .clickable { onHomeClick() },
                        bitmap = homeImg,
                        contentDescription = null
                    )
                }

                Box(contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier
                            .padding(start = 70.dp, end = 70.dp)
                            .size(60.dp, 60.dp)
                            .clickable { onAddEventClick() },
                        imageVector = addImg,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        modifier = Modifier
                            .padding( end = 30.dp)
                            .size(40.dp, 40.dp)
                            .clickable { onListClick() },
                        bitmap = listImg,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

            }
        }
    }}



