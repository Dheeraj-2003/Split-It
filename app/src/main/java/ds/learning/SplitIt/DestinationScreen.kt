package ds.learning.SplitIt

sealed class DestinationScreen(val route:String) {
    object SignUp:DestinationScreen("signup")
    object Login:DestinationScreen("login")
    object Profile:DestinationScreen("profile")
    object splashScreen:DestinationScreen("splash")
    object ChatList:DestinationScreen("chatlist")
    object StatusList:DestinationScreen("statuslist")
    object SingleChat:DestinationScreen("singlechat/{id}"){
        fun createRoute(id:String) = "singlechat/$id"
    }
    object SingleStatus:DestinationScreen("singlestatus/{id}"){
        fun createRoute(id:String) = "singlestatus/$id"
    }

    sealed class BottomScreen(val route: String){
        object ChatList:DestinationScreen("chatlist")
        object StatusList:DestinationScreen("statuslist")
        object Profile:DestinationScreen("profile")
    }
}