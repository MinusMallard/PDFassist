package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodels.ViewModelProviderImpl

@Composable
fun SettingScreen(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    var newPathValue by remember { mutableStateOf("") }
    val paths = viewModelProviderImpl.getFileHandlerViewModel().paths.collectAsState().value
    LaunchedEffect(true) {
        viewModelProviderImpl.getFileHandlerViewModel().getPaths()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Text(
            text = "Path marked for scanning",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black, shape = RoundedCornerShape(4.dp))
                .border(1.dp, Color.White, RoundedCornerShape(4.dp))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(paths) { path ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• $path",
                            color = Color.White,
                            fontSize = 14.sp,
                            softWrap = true
                        )
                    }
                    Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = newPathValue,
                onValueChange = { newPathValue = it },
                modifier = Modifier
                    .weight(1f),
                singleLine = true,
                placeholder = {
                    Text("Add path for scanning", color = Color.Gray)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    textColor = Color.White
                ),
                textStyle = TextStyle(fontSize = 14.sp),
                shape = RoundedCornerShape(4.dp)
            )
            Button(
                onClick = {
                    viewModelProviderImpl.getFileHandlerViewModel().addPath(newPathValue.trim())
                    newPathValue = ""
                },
                modifier = Modifier.height(50.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Add Path",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}