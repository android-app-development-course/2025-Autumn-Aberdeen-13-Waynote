package com.example.waynote

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(
    val route: String,
    val labelEn: String,
    val labelZh: String,
    val icon: ImageVector
) {
    Home(AppDestination.Home.route, "Home", "首页", Icons.Outlined.Home),
    Messages(AppDestination.Messages.route, "Messages", "消息", Icons.Outlined.Inbox),
    Profile(AppDestination.Profile.route, "Profile", "我的", Icons.Outlined.Person)
}

fun MainTab.localizedLabel(language: AppLanguage): String {
    return localizedText(labelEn, labelZh, language)
}

@Composable
fun MainTab.localizedLabel(): String {
    return localizedText(labelEn, labelZh)
}

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object Home : AppDestination("home")
    data object Messages : AppDestination("messages")
    data object Profile : AppDestination("profile")
    data object Favorites : AppDestination("favorites")
    data object TripPlanner : AppDestination("trip_planner")
    data object SavedTrip : AppDestination("saved_trip")
    data object CommunityGuides : AppDestination("community_guides")
    data object FlightBooking : AppDestination("flight_booking")
    data object Orders : AppDestination("orders")
    data object DestinationDetail : AppDestination("destination_detail")
    data object Chat : AppDestination("chat")
}
