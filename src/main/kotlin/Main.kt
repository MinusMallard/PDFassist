import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.SettingScreen
import ui.tabBar
import viewmodels.ViewModelProviderImpl

@Composable
@Preview
fun App(
    viewModelProviderImp: ViewModelProviderImpl
) {
    val windowStateManagementController = viewModelProviderImp.getWindowsStateManagement();
    val isSettingsVisible = windowStateManagementController.isSettingsVisible.collectAsState().value
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box (
                modifier = Modifier.weight(1f)
            ) {
                if (isSettingsVisible) {
                    SettingScreen(viewModelProviderImp)
                } else {

                }
            }
            tabBar(
                viewModelProviderImp,
                windowStateManagementController
            )
        }
    }
}

// Need to implement the factory class which instantiates the
fun main() = application {
    val windowState = rememberWindowState()
    val viewModelProviderImpl = ViewModelProviderImpl()
    viewModelProviderImpl.getTabBarViewModel().addNewTab()
    viewModelProviderImpl.getFileHandlerViewModel().load()
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "PDFassist",
    ) {
        MaterialTheme {
                App(
                    //windowsState = windowState,
                    viewModelProviderImp = viewModelProviderImpl
                )
        }
    }
}
