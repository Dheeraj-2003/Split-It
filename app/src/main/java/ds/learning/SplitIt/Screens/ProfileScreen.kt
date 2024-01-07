package ds.learning.SplitIt.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ds.learning.SplitIt.DestinationScreen
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.Utils.CommonImage
import ds.learning.SplitIt.Utils.commonProgressBar
import ds.learning.SplitIt.Utils.commondivider
import ds.learning.SplitIt.Utils.titleText

@Composable
fun ProfileScreen(navController: NavController, viewModel : LCViewModel){
    var inProgess = viewModel.inProgress.value
    if(inProgess){
        commonProgressBar()
    }
    else{
        val userData = viewModel.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name?:"")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number?:"")
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
           ProfileContent(
               modifier = Modifier
                   .weight(1f)
                   .verticalScroll(rememberScrollState())
                   .padding(8.dp),
               name = name,
               number = number,
               onNameChange = {name = it} ,
               onNumberChange = {number = it} ,
               onBack = { navController.navigate(DestinationScreen.ChatList.route) },
               onSave = { viewModel.createOrUpdateProfile(name = name,number = number) },
               viewModel = viewModel,
               onLogout = {viewModel.logout()
               navController.navigate(DestinationScreen.Login.route)}
           )
            BottomNavigationMenu(selectedIcon = BottonNavigationItem.Profile, navController = navController)
        }
    }
}

@Composable
fun ProfileContent(
    onLogout:()->Unit,
    modifier: Modifier = Modifier,
    name: String,number: String, onNameChange: (String)->Unit, onNumberChange: (String)->Unit,
    onBack:()->Unit,onSave:()->Unit,viewModel: LCViewModel){

    val imageUrl = viewModel.userData.value?.imageUrl

    Column(modifier = Modifier.wrapContentHeight(),horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Back",
                Modifier
                    .padding(20.dp)
                    .clickable {
                        onBack.invoke()
                    })
            titleText(text = "Profile")
            Text(text = "Save",
                Modifier
                    .padding(20.dp)
                    .clickable {
                        onSave.invoke()
                    })
        }
        commondivider()
        ProfileImage(imageUrl,viewModel)
        commondivider()

        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Name: ",
                Modifier
                    .padding(top = 20.dp)
                    .width(100.dp))
            TextField(value = name, onValueChange = onNameChange ,colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent
            ))
        }

        Row(modifier = Modifier
            .padding(8.dp, bottom = 20.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Number: ",
                Modifier
                    .padding(top = 20.dp)
                    .width(100.dp))
            TextField(value = number, onValueChange = onNumberChange ,colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent
            ))
        }

        commondivider()

        Button(onClick = {
                         onLogout()
                         },modifier = Modifier.padding(10.dp)) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun ProfileImage(imageUrl:String?,viewModel: LCViewModel){
    
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
            viewModel.uploadProfileImage(uri)
        }
    }

    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            }, horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")
        }
        if(viewModel.inProgress.value){
            commonProgressBar()
        }
    }
    
}