package ds.learning.SplitIt.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ds.learning.SplitIt.DestinationScreen
import ds.learning.SplitIt.R

enum class BottonNavigationItem(val icon: Int, val navDestination:DestinationScreen){

    Chatlist(R.drawable.chat_list,DestinationScreen.ChatList),
    Statuslist(R.drawable.split,DestinationScreen.StatusList),
    Profile(R.drawable.user,DestinationScreen.Profile)

}


@Composable
fun BottomNavigationMenu(
    selectedIcon: BottonNavigationItem,
    navController: NavController
){

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(MaterialTheme.colorScheme.background), horizontalArrangement = Arrangement.SpaceEvenly) {
        
        for(item in BottonNavigationItem.values()){
            
            IconButton(onClick = { navController.navigate(item.navDestination.route) },modifier = Modifier.size(50.dp).padding(8.dp).weight(1f)) {
                Image(painter = painterResource(id = item.icon), contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground), alpha = if(selectedIcon==item) 1f else 0.5f)
            }
            
        }

    }

}