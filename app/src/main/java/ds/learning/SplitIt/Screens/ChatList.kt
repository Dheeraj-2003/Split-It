package ds.learning.SplitIt.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ds.learning.SplitIt.DestinationScreen
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.Utils.CommonRow
import ds.learning.SplitIt.Utils.commondivider
import ds.learning.SplitIt.Utils.titleText

@Composable
fun ChatListScreen(navController: NavController,viewModel : LCViewModel){

    val inProgress = viewModel.inProgress
    val chats = viewModel.chats.value
    val UserData = viewModel.userData.value
    val showDialog = remember {
        mutableStateOf(false)
    }
    val onFABclick:()->Unit = {showDialog.value = true}
    val onDismiss:()->Unit = {showDialog.value = false}
    val onAddChat:(String)->Unit={
        viewModel.onAddChat(it)
        showDialog.value = false
    }
    
    Scaffold(
        floatingActionButton = { FAB(showDialog = showDialog.value, onDismiss = { onDismiss() }, onAddChat = onAddChat,onFABclick={onFABclick()}) }
    ,
        bottomBar = {
            BottomNavigationMenu(selectedIcon = BottonNavigationItem.Chatlist, navController = navController)
        },) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            titleText(text = "Chats")
            commondivider()
            if(chats.isEmpty()){
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "No Chats Available")
                }
            }else{
                LazyColumn{
                    items(chats){
                        chat->
                        val chatUser = if(chat.user1.userId == UserData?.userId){
                            chat.user2
                        }else{
                            chat.user1
                        }
                        CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name,{viewModel.onDeleteChat(chatId = chat.chatId?:"")}) {
                            chat.chatId?.let{
                                navController.navigate(DestinationScreen.SingleChat.createRoute(id=it))
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun FAB(
    showDialog:Boolean,
    onDismiss:()->Unit,
    onAddChat:(String)->Unit,
    onFABclick:()->Unit
){
    val addChatNumber = remember{
        mutableStateOf("")
    }

    AnimatedVisibility(showDialog){
        AlertDialog(onDismissRequest = {
                                       onDismiss()
            addChatNumber.value=""
                                       },
            modifier = Modifier.animateEnterExit(enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ), confirmButton = {
                                           Button(onClick = {
                                               onAddChat(addChatNumber.value)
                                               addChatNumber.value = ""
                                           }) {
                                                Text(text = "Add Chat")
                                           }
        },title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(value = addChatNumber.value , onValueChange = {addChatNumber.value=it}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            })
    }

    FloatingActionButton(
        onClick = { onFABclick() },
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = CircleShape,
        modifier = Modifier
            .padding(bottom = 20.dp, end = 10.dp)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null , tint = MaterialTheme.colorScheme.onBackground)
    }
}


