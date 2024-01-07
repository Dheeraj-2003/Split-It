package ds.learning.SplitIt.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.Utils.CommonImage
import ds.learning.SplitIt.Utils.commondivider
import ds.learning.SplitIt.data.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SingleChatScreen(navController: NavController, viewModel: LCViewModel, chatId: String) {

    var message by remember {
        mutableStateOf("")
    }

    fun onSend() {
        viewModel.onSend(chatId, message)
        message = ""
    }

    val myUser = viewModel.userData.value
    val currentChat = viewModel.chats.value.first{
        it.chatId == chatId
    }
    val chatUser = if(myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

    val chatMessages = viewModel.chatMessages.value

    LaunchedEffect(key1 = Unit){
        viewModel.populateMessages(chatId)
    }
    BackHandler {
        navController.popBackStack()
        viewModel.depopulateMessages()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .imePadding()
        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(9f)) {
            ChatTop(name = chatUser.name?:"", imageUrl = chatUser.imageUrl?:"",chatId,viewModel = viewModel, navController = navController) {
                navController.popBackStack()
                viewModel.depopulateMessages()
            }
            MessageBox(modifier = Modifier.weight(1f), chatMessages = chatMessages, currentUserId = myUser?.userId?:"")
        }
        Column(modifier = Modifier.weight(1f)) {
            ReplyBox(message = message, onMessageChange = {message=it}) {
                onSend()
            }
        }
    }



}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReplyBox(message: String, onMessageChange: (String) -> Unit, onSend: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxWidth()
        .imePadding()) {
        commondivider()
        val bringIntoViewRequester = remember {
            BringIntoViewRequester()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 2.dp)
        ) {
            TextField(
                value = message,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(7f)
                    .padding(horizontal = 10.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                placeholder = {
                    Text(
                        text = "Message",
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.background
                )
            )
            IconButton(
                onClick = { if(message.trim().isNotEmpty()){ onSend() } }, modifier = Modifier
                    .padding(top = 5.dp, end = 8.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "send",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun ChatTop(name: String, imageUrl: String, chatId: String, viewModel: LCViewModel, navController: NavController,onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.wrapContentWidth()) {
            IconButton(onClick = { onBackClick() }, modifier = Modifier.padding(top = 8.dp)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
            CommonImage(
                data = imageUrl, modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 4.dp)
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
            )
            Text(text = name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp, top = 18.dp), fontSize = 20.sp)

        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBox(modifier: Modifier, chatMessages:List<Message>, currentUserId: String){

    commondivider()
    LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.Bottom){
        items(chatMessages){
            message->
            val alignment = if(message.sendBy==currentUserId) Alignment.End else Alignment.Start
            val shape = if(message.sendBy==currentUserId) RoundedCornerShape(topStart = 20.dp, topEnd = 10.dp, bottomStart = 10.dp) else RoundedCornerShape(bottomEnd = 20.dp,  bottomStart = 10.dp, topEnd = 10.dp)
            val color = if(message.sendBy==currentUserId) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer
            Column(modifier = Modifier
                .fillMaxSize()
                .animateItemPlacement(tween(durationMillis = 250))
                .padding(8.dp), horizontalAlignment = alignment) {
                Text(text = message.messageval?:"",modifier = Modifier
                    .clip(shape)
                    .background(color)
                    .padding(12.dp)
                    .widthIn(max = 250.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}
