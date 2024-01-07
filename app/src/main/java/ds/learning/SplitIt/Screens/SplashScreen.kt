package ds.learning.SplitIt.Screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ds.learning.SplitIt.DestinationScreen
import ds.learning.SplitIt.LCViewModel
import ds.learning.SplitIt.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController:NavController,viewModel: LCViewModel){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        val scale = remember {
            Animatable(0f)
        }

        LaunchedEffect(key1 = true, block = {
            scale.animateTo(targetValue = 0.6f,
                animationSpec = tween(800,
                    easing = {
                        OvershootInterpolator(8f).getInterpolation(it)
                    })
            )
            delay(1500L)
            val signedIn = viewModel.signIn
            if(signedIn.value==true){
                navController.navigate(DestinationScreen.StatusList.route)
            }
            else{
                navController.navigate(DestinationScreen.SignUp.route)
            }
        })

        Surface(modifier = Modifier
            .padding(10.dp)
            .size(400.dp)
            .scale(scale.value), shape = CircleShape
        ) {
            Column(modifier = Modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Image(painter = painterResource(id = R.drawable.bill),contentDescription = null, contentScale = ContentScale.Inside, colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onBackground), modifier = Modifier.padding(bottom = 40.dp))
                Text(text = "Split-It", fontSize = 40.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,modifier = Modifier.padding(10.dp), fontFamily = FontFamily.Monospace)
            }
        }
    }
}