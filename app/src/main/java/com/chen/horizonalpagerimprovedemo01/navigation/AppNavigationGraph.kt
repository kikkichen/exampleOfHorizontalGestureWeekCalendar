@file:Suppress("UNCHECKED_CAST")

package com.chen.horizonalpagerimprovedemo01.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chen.horizonalpagerimprovedemo01.view.ImproveScreen
import com.chen.horizonalpagerimprovedemo01.view.MainScreen
import com.chen.horizonalpagerimprovedemo01.view.WeekScreen
import com.chen.horizonalpagerimprovedemo01.viewmodel.ImproveViewModel
import com.chen.horizonalpagerimprovedemo01.viewmodel.MainViewModel
import com.chen.horizonalpagerimprovedemo01.viewmodel.WeekViewModel

@Composable
fun AppNavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainRouteGraph.MainScreen.route
    ) {
        composable(route = MainRouteGraph.MainScreen.route) {
            val viewModel = viewModel<MainViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel() as T
                    }
                }
            )
            MainScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = MainRouteGraph.ImproveScreen.route) {
            val viewModel = viewModel<ImproveViewModel>()
            ImproveScreen(viewModel = viewModel)
        }
        composable(route = MainRouteGraph.WeekScreen.route) {
            val viewModel = viewModel<WeekViewModel>()
            WeekScreen(viewModel = viewModel)
        }
    }
}