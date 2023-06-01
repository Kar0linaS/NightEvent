package com.dziubi.nolio.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dziubi.nolio.R

@Composable
fun LoginScreen(
    onClickLogin: (String, String) -> Unit,

) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {


        Text(modifier = Modifier
            .padding(vertical = 16.dp),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.logg_in_to_your_account)
                    )


        EmailTextField(
            text = email,
            textLabel = "example@email.com",
            onValueChange = { str -> email = str })

        PasswordTextField(
            text = password,
            textLabel =  "*******" ,
            onValueChange = { str -> password = str})

        OutlinedButton(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick =  {onClickLogin(email, password) }
        ) {
            Text(text = stringResource(R.string.logg_in), color = Color.DarkGray)
        }
    }

}

@Composable
fun PasswordTextField(
    text: String,
    textLabel: String,
    onValueChange: (String) -> Unit,
) {

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Column {
        Text(modifier = Modifier
            .padding(start = 16.dp),
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            text = stringResource(R.string.password))

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            value = text,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(percent = 10),
            label = { Text(text = textLabel) },
            leadingIcon = {
                val emailIcon = Icons.Filled.Email
                Icon(imageVector = emailIcon,
                    contentDescription = null)
            },
            visualTransformation =
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)

                }
            }

        )
    }
}

@Composable
fun EmailTextField(
    text: String,
    textLabel: String,
    onValueChange: (String) -> Unit,
) {

    Column {
        Text(modifier = Modifier
            .padding(start = 16.dp),
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            text = stringResource(R.string.email))

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            value = text,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(percent = 10),
            label = {
                Text(text = textLabel) },
            leadingIcon = {
                val emailIcon = Icons.Filled.Email
                Icon(imageVector = emailIcon,
                    contentDescription = null)
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onClickLogin = { s1, s2 -> })
}