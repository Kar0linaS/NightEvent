package com.dziubi.nolio.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartScreen(
    onLoginClick: () -> Unit = {},
    onClick: () -> Unit = {},
    onRegistrationClick: () -> Unit = {}
) {


    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(modifier = Modifier.padding(bottom = 10.dp),
            text = "Night\n in the \n City",
            color = Color.Blue,
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold
        )
        Text(modifier = Modifier.padding(top = 10.dp, bottom = 40.dp),
            text ="Night event in your city",
            color = Color.DarkGray,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedButton(modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(48.dp),
            onClick = onClick
        ) {
            val iconArrow = ImageVector.vectorResource(id = com.dziubi.nolio.R.drawable.ic_arrow_right)

            Row(
                verticalAlignment = Alignment.CenterVertically,

                ){
                Text(
                    text = "Zaloguj się przez Facebook",
                    color = Color.DarkGray,
                    fontSize = 15.sp
                )
                Icon(modifier = Modifier
                    .padding(start = 10.dp),
                    imageVector = iconArrow,
                    contentDescription = null,
                    tint = Color.DarkGray)
            }
        }

        OutlinedButton(modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 50.dp)
            .fillMaxWidth()
            .height(48.dp),
            onClick = onLoginClick
        ) {
            val iconArrow = ImageVector.vectorResource(id = com.dziubi.nolio.R.drawable.ic_arrow_right)

            Row(verticalAlignment = Alignment.CenterVertically,
                ){
                Text(
                    text = "Zaloguj się",
                    color = Color.DarkGray,
                    fontSize = 15.sp
                )
                Icon(modifier = Modifier
                    .padding(start = 10.dp),
                    imageVector = iconArrow,
                    contentDescription = null,
                    tint = Color.DarkGray)
            }
        }
         Row(modifier = Modifier.padding(start = 60.dp, end = 16.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
             Box(contentAlignment = Alignment.BottomEnd){
                 Text(text = "Nie masz konta?")
             }
             Text(modifier = Modifier
                 .padding(start = 10.dp, end = 16.dp)
                 .clickable { onRegistrationClick() },
                 text = "Rejestracja",
                color = Color.Blue,
             fontWeight = FontWeight.Bold,
             textDecoration = TextDecoration.Underline)
         }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen()

}