package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodels.ViewModelProviderImpl

@Composable
fun TabScreen(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    val tabBarViewModel = viewModelProviderImpl.getTabBarViewModel()

    if (tabBarViewModel.getTab()) {
        ShowFiles(
            viewModelProviderImpl = viewModelProviderImpl,
        )
    } else {
        ShowFiles(
            viewModelProviderImpl = viewModelProviderImpl,
        )
    }
}

@Composable
fun ShowFiles(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    val files = viewModelProviderImpl.getFileHandlerViewModel().files.collectAsState().value
    LaunchedEffect(true) {
        viewModelProviderImpl.getFileHandlerViewModel().getFiles()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(files) {file ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {},
                    ) {
                        Text(
                            text = file.toString().substringAfterLast("\\"),
                            fontSize = 12.sp,
                            lineHeight = 10.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RenderPDF(

) {

}