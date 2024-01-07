package ds.learning.SplitIt.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ds.learning.SplitIt.DestinationScreen
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.Utils.CommonRowSplit
import ds.learning.SplitIt.Utils.commondivider
import ds.learning.SplitIt.Utils.titleText

@Composable
fun StatusScreen(navController: NavController, viewModel : LCViewModel){

    val showDialog = remember {
        mutableStateOf(false)
    }
    val onFABclick:()->Unit = {showDialog.value = true}
    val onDismiss:()->Unit = {showDialog.value = false}
    val onConfirm:(String)->Unit={
        viewModel.createGroup(it)
        showDialog.value = false
    }
    val groups = viewModel.groups.value

    Scaffold(
        floatingActionButton = { FABSplit(showDialog = showDialog.value, onDismiss = { onDismiss() }, onConfirm = onConfirm,onFABclick={onFABclick()}) }
        ,
        bottomBar = {
            BottomNavigationMenu(selectedIcon = BottonNavigationItem.Statuslist, navController = navController)
        },) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            titleText(text = "Split-It")
            commondivider()
            Text(text = "Groups", fontWeight = FontWeight.SemiBold, fontSize = 23.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding( vertical = 8.dp, horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary)
            if(groups.isEmpty()){
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "No Groups Available")
                }
            }else{
                LazyColumn{
                    items(groups){
                        group->
                        CommonRowSplit(imageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/chatapp-91cb0.appspot.com/o/images%2F55dfd90f-4d85-4d58-8fa9-538e002ddbd0?alt=media&token=11838abe-5a8c-4773-ae8d-9fb3ffe30e07", name = group.groupName) {
                            navController.navigate(DestinationScreen.SingleStatus.createRoute(id=group.groupId?:""))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun FABSplit(
    showDialog:Boolean,
    onDismiss:()->Unit,
    onConfirm:(String)->Unit,
    onFABclick:()->Unit
){
    val groupNumber = remember{
        mutableStateOf("")
    }

    val numberOfMembers = remember {
        mutableStateOf("")
    }

    val listOfmembers = remember {
        mutableStateListOf<String>()
    }

    if(showDialog){
        AlertDialog(onDismissRequest = {
            onDismiss()
            groupNumber.value=""
        }, confirmButton = {
            Button(onClick = {
                onConfirm(groupNumber.value)
                groupNumber.value = ""
            }) {
                Text(text = "Create Group")
            }
        },title = { Text(text = "Create Group") },
            text = {
                OutlinedTextField(value = groupNumber.value , onValueChange = {groupNumber.value=it})
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
