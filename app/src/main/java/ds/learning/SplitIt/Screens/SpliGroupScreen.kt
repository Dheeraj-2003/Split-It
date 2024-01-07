package ds.learning.SplitIt.Screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.Utils.CommonImage
import ds.learning.SplitIt.Utils.commondivider

@Composable
fun SingleGroupScreen(navController: NavController, viewModel: LCViewModel, groupId: String) {



    val myUser = viewModel.userData.value

    val group = viewModel.groups.value.find {
        it.groupId == groupId
    }

    val onAddMembertoGroup:(String)->Unit = {
        viewModel.addMembertoGroup(groupId,it)
    }

    var selected by remember {
        mutableStateOf("members")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var payBy by remember {
        mutableStateOf("")
    }

    val onAddExpense:(Int,String)->Unit = {
        amountt, numberr ->
        viewModel.addExpense(amountt,numberr,groupId)
    }

    var members by remember {
        mutableStateOf(group?.members)
    }

    var trans by remember {
        mutableStateOf(viewModel.trans)
    }

    var memToIdx by remember {
        mutableStateOf(group?.memberToIndex)
    }

    var n by remember {
        mutableStateOf(members?.size!!)
    }


    LaunchedEffect(key1 = Unit){
        viewModel.populateExpense(groupId)
        trans = viewModel.trans
    }
    BackHandler {
        navController.popBackStack()
        //Todo
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .imePadding()
        .verticalScroll(rememberScrollState())) {
        GroupTop(name = group?.groupName?:"", imageUrl = "https://firebasestorage.googleapis.com/v0/b/chatapp-91cb0.appspot.com/o/images%2F55dfd90f-4d85-4d58-8fa9-538e002ddbd0?alt=media&token=11838abe-5a8c-4773-ae8d-9fb3ffe30e07", onAddMembertoGroup = onAddMembertoGroup) {
            navController.popBackStack()
            //Todo
        }
        commondivider()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { selected = "members" },modifier = Modifier.alpha(if(selected == "members") 1f else 0.5f)) {
                Text(text = "Members")
            }
            TextButton(onClick = { selected = "expenses" },modifier = Modifier.alpha(if(selected == "expenses") 1f else 0.5f)) {
                Text(text = "Expenses")
            }
        }
        Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f), modifier = Modifier.padding(8.dp))
        if(selected == "members"){
            val items = group?.members!!
            Column(modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())) {
                for (it in items){
                    RowMembers(imageUrl = it.imageUrl, name = it.name, id = it.number?:"")
                }
            }
        }
        else{
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp,
                    ), modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize(1f),horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Add Expense",
                            modifier = Modifier
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp, fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(value = amount, onValueChange = {amount = it}, label = { Text(
                            text = "Amount ( Rs. )"
                        )}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number
                        ))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)) {
                            OutlinedTextField(value = payBy, onValueChange = {payBy = it}, label = { Text(
                                text = "Payed By ( Id )"
                            )}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        }
                        Button(onClick = {
                                         if(payBy.isNotEmpty() and amount.isNotEmpty()){
                                             onAddExpense(amount.toInt(),payBy)
                                             amount = ""
                                             payBy = ""
                                             trans = viewModel.trans
                                             Log.d("DEB","Size of trans is ${trans.size}")
                                         }
                        }, modifier = Modifier.padding(bottom = 24.dp)) {
                            Text(text = "Add Expense")
                        }
                    }
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {
                    if(trans.size>1){

                        for(i in 0 until n){
                            for ( j in 0 until n){
                                if(trans[i][j] != 0){
                                    val number1 = memToIdx?.filterValues {
                                        it == i
                                    }?.keys?.first()
                                    val number2 = memToIdx?.filterValues {
                                        it == j
                                    }?.keys?.first()
                                    val a = members?.find { it.number == number1 }?.name
                                    val b = members?.find { it.number == number2 }?.name
                                    val users = Pair(a?:"",b?:"")
                                    Card(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp), shape = RoundedCornerShape(15.dp)
                                    ) {
                                        Box(modifier = Modifier
                                            .fillMaxWidth().clickable {
                                                //Implement Settle Up
                                            }
                                            .padding(8.dp)){
                                            Text(text = "${users.first} owes ${users.second} Rs. ${trans[i][j]}")
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }
    }



}



@Composable
fun GroupTop(name: String, imageUrl: String,onAddMembertoGroup:(String) ->Unit , onBackClick: () -> Unit) {
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

        addIcon(showDialog = false, onAddMembertoGroup = onAddMembertoGroup)

    }
}

@Composable
fun addIcon(showDialog:Boolean ,onAddMembertoGroup: (String) -> Unit){

    var toShow by remember {
        mutableStateOf(showDialog)
    }

    val addMember = remember{
        mutableStateOf("")
    }

    if(toShow){
        AlertDialog(onDismissRequest = {
            toShow = false
            addMember.value=""
        }, confirmButton = {
            Button(onClick = {
                if(addMember.value!=""){
                    onAddMembertoGroup(addMember.value)
                    addMember.value = ""
                    toShow = false
                }
            }) {
                Text(text = "Add Member")
            }
        },title = { Text(text = "Add Member") },
            text = {
                OutlinedTextField(value = addMember.value , onValueChange = {addMember.value=it}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            })
    }

    IconButton(onClick = {
        toShow = true
    }) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
    }

}

@Composable
fun RowMembers(imageUrl:String?,name:String?, id:String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .height(70.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier
            .height(70.dp), verticalAlignment = Alignment.CenterVertically) {
            CommonImage(data = imageUrl ,modifier = Modifier
                .padding(8.dp)
                .size(55.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground))
            Text(text = name?:"---", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(4.dp), fontSize = 20.sp)
        }
        Row {
            Text(text = "ID: $id", color = MaterialTheme.colorScheme.primary)
        }
    }
}

