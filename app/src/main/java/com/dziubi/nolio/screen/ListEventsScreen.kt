package com.dziubi.nolio.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dziubi.nolio.R
import com.dziubi.nolio.data.models.EventModel


@Composable
fun ListEventsScreen(
    events: List<EventModel>
) {
    Column(){
        Text(modifier = Modifier
            .padding(start = 16.dp, top = 10.dp),
            text = stringResource(R.string.List_of_events),
        color = Color.Gray,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp)
        LazyColumn(modifier = Modifier.padding(start = 16.dp, top = 20.dp)){
            items(items = events){event ->
                EventItem(event = event)
            }
        }
    }
}

@Composable
fun EventItem(
    event: EventModel
) {
    event.desc?.let {
        Text(
        text = it,
            fontSize = 15.sp,
            color = Color.DarkGray
    )
    }

}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListEventsScreenPreview() {
    ListEventsScreen(events = listOf(EventModel(desc = "jjdjdksljajl;jfklfjklsdjfgld;skgl;sdkgoskl;l")))

}