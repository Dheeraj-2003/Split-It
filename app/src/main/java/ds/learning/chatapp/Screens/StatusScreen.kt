package ds.learning.chatapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import ds.learning.chatapp.LCViewModel
import ds.learning.chatapp.Utils.titleText

@Composable
fun StatusScreen(navController: NavController, viewModel : LCViewModel){
    val showDialog = remember {
        mutableStateOf(false)
    }
    Scaffold(floatingActionButton = { FABStat() }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
            titleText(text = "Status")
            Text(text = "No status updates")
            BottomNavigationMenu(selectedIcon = BottonNavigationItem.Statuslist, navController = navController)
        }
    }
}

@Composable
fun FABStat(
){
    FloatingActionButton(
        onClick = {  },
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 70.dp, end = 10.dp)
    ) {
        Icon(imageVector = Icons.Default.Edit, contentDescription = null , tint = MaterialTheme.colorScheme.onBackground)
    }
}