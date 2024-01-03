package ds.learning.chatapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ds.learning.chatapp.DestinationScreen
import ds.learning.chatapp.LCViewModel
import ds.learning.chatapp.R
import ds.learning.chatapp.Utils.CheckSignedIn
import ds.learning.chatapp.Utils.commonProgressBar

@Composable
fun LoginScreen(viewModel:LCViewModel,navController: NavController){

    CheckSignedIn(viewModel = viewModel, navController = navController)

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            ), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.chat)
                , contentDescription = "AppIcon" , modifier = Modifier
                    .width(150.dp)
                    .padding(16.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Text(text = "Convo",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(value = emailState.value, onValueChange = {
                emailState.value = it
            }, Modifier.padding(8.dp)
                , label = { Text(text = "Email")})
            OutlinedTextField(value = passwordState.value, onValueChange = {
                passwordState.value = it
            }, Modifier.padding(8.dp)
                , label = { Text(text = "Password")})
            Button(onClick = {
                viewModel.Login(emailState.value,passwordState.value)
            },modifier = Modifier.padding(8.dp)) {
                Text(text = "Sign In")
            }

        }

        if(viewModel.inProgress.value)
            commonProgressBar()
    }
}