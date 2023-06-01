package com.dziubi.nolio.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.dziubi.nolio.R
import com.dziubi.nolio.data.models.EventModel
import com.dziubi.nolio.data.models.UserModel

@Composable
fun ProfileScreen(
    data: UserModel,
    events: List<EventModel>,
    onCloseClick: () -> Unit = {}
) {


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        ImageProfile(onCloseClick = onCloseClick)
        UserData(
            name = data.name,
            email = data.email)
        ListEvent(
            events = events)
    }
}

@Composable
fun ImageProfile(
    onCloseClick: () -> Unit
) {

    val imageProfile = ImageBitmap.imageResource(id = R.drawable.profile_img)
    val closeImage = ImageVector.vectorResource(id = R.drawable.ic_close)

    var showLogoutDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.padding(top = 50.dp, bottom = 50.dp)) {
        Row(){
        Box(contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier
                    .size(150.dp, 150.dp),
                bitmap = imageProfile,
                contentDescription = null)
        }
          Surface(){
              Box(contentAlignment = Alignment.TopEnd){
                  Image(modifier = Modifier
                      .clickable { showLogoutDialog = true  },
                      imageVector = closeImage,
                      contentDescription = null,
                     colorFilter = ColorFilter.tint(Color.DarkGray))

                  if (showLogoutDialog)
                      LogOutDialog(
                          onDismiss = { showLogoutDialog = false },
                          onExit = {
                              showLogoutDialog = false
                              onCloseClick()
                          })
              }
          }
        }
    }
}

@Composable
fun UserData(
    name: String,
    email: String,
) {
    Column() {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier
                .padding(start = 16.dp),
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }
        Row(modifier = Modifier.padding(top = 10.dp, end = 30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier
                .padding(start = 16.dp),
                text = email,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray)
        }
    }
}

@Composable
fun ListEvent(
    events: List<EventModel>,
) {

    Text(modifier = Modifier.padding(start = 16.dp, top = 10.dp),
        text = stringResource(R.string.your_incidents),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.Gray)
    LazyColumn() {
        items(items = events) { event ->
            ItemEvent(event = event)
        }
    }
}


@Composable
fun ItemEvent(
    event: EventModel,
) {
    Row(modifier = Modifier.padding(start = 16.dp, top = 10.dp)) {
        event.desc?.let { Text(text = it) }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        data = UserModel(
            "fdagsfgsadg", "Alicja Zukowska-Czajkowska", "zukowska_czajkowska@gmail.com"
        ),
        events = listOf(
            EventModel(desc = "Description")
        )
    )
}