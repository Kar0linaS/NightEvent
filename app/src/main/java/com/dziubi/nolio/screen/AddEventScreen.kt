package com.dziubi.nolio.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dziubi.nolio.R


@Composable
fun AddEventScreen(
    AddEventCLick: (String) -> Unit = {},
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        EnterTextField(
            modifier = Modifier.weight(0.75f),
            text = text,
            textLabel = stringResource(R.string.enter_events_detail),
            onTextChange = { str -> text = str })
        AddEventButton(
            modifier = Modifier.weight(0.25f),
            AddEvent = {AddEventCLick(text)}
        )
    }
}



@Composable
fun EnterTextField(
    modifier: Modifier = Modifier,
    text: String,
    textLabel: String,
    onTextChange: (String) -> Unit,

    ) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        value = text,
        onValueChange = onTextChange,
        label = { Text(text = textLabel) })

}

@Composable
fun AddEventButton(
    modifier: Modifier = Modifier,
    AddEvent: () -> Unit,
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row() {
            OutlinedButton(onClick = { AddEvent() }) {
                Text(
                    text = "DODAJ"
                )
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen()

}