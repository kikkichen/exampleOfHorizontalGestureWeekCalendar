package com.chen.horizonalpagerimprovedemo01.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chen.horizonalpagerimprovedemo01.navigation.MainRouteGraph
import com.chen.horizonalpagerimprovedemo01.viewmodel.MainViewModel

/**
 *  主页
 */
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Column() {
        Text(text = "Hello world")
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            navController.navigate(route = MainRouteGraph.ImproveScreen.route)
        }) {
            Text(text = "打开分页测试")
        }
        Button(onClick = {
            navController.navigate(route = MainRouteGraph.WeekScreen.route)
        }) {
            Text(text = "打开周历")
        }
    }
}