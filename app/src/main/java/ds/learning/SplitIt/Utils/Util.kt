package ds.learning.SplitIt.Utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import ds.learning.SplitIt.DestinationScreen
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.data.SplitUser

@Composable
fun commonProgressBar(){
    Row(modifier = Modifier
        .alpha(0.5f)
        .clickable(false) {}
        .fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

        CircularProgressIndicator()
    }
}

@Composable
fun CheckSignedIn(viewModel: LCViewModel, navController: NavController){
    val alreadySignedIn = remember {
        mutableStateOf(false)
    }
    val signin = viewModel.signIn.value
    if(signin && !alreadySignedIn.value){
        alreadySignedIn.value = true
        navController.navigate(DestinationScreen.ChatList.route){
            popUpTo(0)
        }
    }
}

@Composable
fun CommonImage(
    data:String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScale: ContentScale = ContentScale.Crop
){
    val painter = rememberImagePainter(data = data)
    Image(painter = painter, contentDescription = null, modifier = modifier, contentScale = contentScale)
}

@Composable
fun commondivider(){
    Divider(thickness = 1.dp, modifier = Modifier
        .alpha(0.3f)
        .padding(vertical = 4.dp))
}

@Composable
fun titleText(text:String){
    Text(text = text, fontWeight = FontWeight.Bold, fontSize = 30.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding( vertical = 8.dp, horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onBackground)
}

@Composable
fun CommonRow(imageUrl:String?,name:String?,onDelete:()->Unit,onClick:()->Unit){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .height(70.dp)
        .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier
            .padding(horizontal = 8.dp)
            .weight(8f)
            .height(70.dp)
            .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
            CommonImage(data = imageUrl ,modifier = Modifier
                .padding(8.dp)
                .size(55.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground))
            Text(text = name?:"---", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(4.dp), fontSize = 20.sp)
        }
        Row(modifier = Modifier.weight(1f)) {
            IconButton(onClick = { onDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }

}

@Composable
fun CommonRowSplit(imageUrl:String?,name:String?,onClick:()->Unit){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .height(70.dp)
        .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        CommonImage(data = imageUrl ,modifier = Modifier
            .padding(8.dp)
            .size(55.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground))
        Text(text = name?:"---", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(4.dp), fontSize = 20.sp)
    }

}

fun usersByIndex(members:MutableList<SplitUser>, I:Int, J:Int, map: MutableMap<String?,Int>):Pair<String,String> {

    val number1 = map.filterValues {
        it == I
    }.keys.first()

    val number2 = map.filterValues {
        it == J
    }.keys.first()

    val a = members.find { it.number == number1 }?.name

    val b = members.find { it.number == number2 }?.name

    return Pair(a?:"",b?:"")

}
