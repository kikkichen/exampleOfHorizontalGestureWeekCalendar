package com.chen.horizonalpagerimprovedemo01.navigation

sealed class MainRouteGraph(
    val name : String,
    val route : String
) {
    object MainScreen: MainRouteGraph(name = "main_screen", route = "MAIN_SCREEN")
    object ImproveScreen: MainRouteGraph(name = "improve_screen", route = "IMPROVE_SCREEN")
    object WeekScreen: MainRouteGraph(name = "week_screen", route = "WEEK_SCREEN")
}
