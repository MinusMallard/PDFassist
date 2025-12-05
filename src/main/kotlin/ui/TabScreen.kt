package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import viewmodels.ViewModelProviderImpl

@Composable
fun TabScreen(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    val tabBarViewModel = viewModelProviderImpl.getTabBarViewModel()

    if (tabBarViewModel.isLoaded.collectAsState().value) {

    } else {

    }
}

@Composable
fun ShowFiles() {

}