package ds.learning.chatapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ds.learning.chatapp.Screens.ChatListScreen
import ds.learning.chatapp.Screens.LoginScreen
import ds.learning.chatapp.Screens.ProfileScreen
import ds.learning.chatapp.Screens.SignUpScreen
import ds.learning.chatapp.Screens.SingleChatScreen
import ds.learning.chatapp.Screens.StatusScreen

@Composable
fun ChatAppNavigation(){
    val navController = rememberNavController()
    val viewModel = hiltViewModel<LCViewModel>()
    NavHost(navController = navController as NavHostController, startDestination = if (viewModel.signIn.value) DestinationScreen.ChatList.route else DestinationScreen.SignUp.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
        ){
        composable(DestinationScreen.SignUp.route,
        ){
            SignUpScreen(navController,viewModel)
        }
        composable(DestinationScreen.Login.route){
            LoginScreen(viewModel,navController)
        }
        composable(DestinationScreen.ChatList.route, enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }){
            ChatListScreen(navController,viewModel)
        }
        composable(DestinationScreen.SingleChat.route){
            val id = it.arguments?.getString("id")
            id?.let {
                SingleChatScreen(navController, viewModel = viewModel,id)
            }
        }
        composable(DestinationScreen.StatusList.route){
            StatusScreen(navController,viewModel)
        }
        composable(DestinationScreen.Profile.route){
            ProfileScreen(navController,viewModel)
        }
    }
}


