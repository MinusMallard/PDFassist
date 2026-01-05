package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodels.ViewModelProviderImpl
@Composable
fun TabScreen(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    val tabBarViewModel = viewModelProviderImpl.getTabBarViewModel()
    val isLoaded by tabBarViewModel.isLoaded.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        if (isLoaded) {
            RenderPDF(viewModelProviderImpl)
        } else {
            ShowFiles(viewModelProviderImpl = viewModelProviderImpl)
        }
    }
}

@Composable
fun ShowFiles(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    val fileHandlerViewModel = viewModelProviderImpl.getFileHandlerViewModel()
    val allFiles by fileHandlerViewModel.files.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredFiles = remember(allFiles, searchQuery) {
        if (searchQuery.isBlank()) {
            allFiles
        } else {
            allFiles.filter {
                it.toString().contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        fileHandlerViewModel.getFiles()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left Pane: History
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = "History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recently used files will appear here",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Divider(modifier = Modifier.width(2.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Search files...", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFiles) { file ->
                    FileItem(
                        fileName = file.fileName.toString(),
                        onClick = {
                            viewModelProviderImpl.getTabBarViewModel().openPDF(file)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileItem(
    fileName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = fileName,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RenderPDF(
    viewModelProviderImpl: ViewModelProviderImpl
) {
    val tabBarViewModel = viewModelProviderImpl.getTabBarViewModel()

    val totalPages = tabBarViewModel.totalPages.collectAsState().value
    val map = tabBarViewModel.bitmaps.collectAsState().value
    val currentActiveTab = tabBarViewModel.curActiveTab.collectAsState().value
    val lazyListStateMap = tabBarViewModel.lazyListState.collectAsState().value
    val currentListState = lazyListStateMap[currentActiveTab]

    val backgroundGray = Color.Black

    // Zoom & Scroll State
    var zoomLevel by remember { mutableStateOf(1f) }
    val horizontalScrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray)
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.isCtrlPressed) {
                    when (keyEvent.key) {
                        Key.Equals, Key.Plus, Key.DirectionUp -> {
                            zoomLevel = (zoomLevel + 0.25f).coerceAtMost(5.0f)
                            true
                        }
                        Key.Minus, Key.DirectionDown -> {
                            zoomLevel = (zoomLevel - 0.25f).coerceAtLeast(1.0f)
                            true
                        }
                        else -> false
                    }
                } else { false }
            }
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val basePadding = 24.dp

        val availableWidth = (screenWidth - (basePadding * 2)) * zoomLevel
        val availableHeight = (screenHeight - (basePadding * 2)) * zoomLevel

        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        if (map.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
                Text("Loading...", color = Color.LightGray)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState)
            ) {
                LazyColumn(
                    state = currentListState,
                    modifier = Modifier
                        .width(screenWidth * zoomLevel)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp * zoomLevel),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = totalPages,
                        key = { index -> index }
                    ) { index ->
                        val pageBitmap = map[index]
                        val bitmapRatio = pageBitmap.width.toFloat() / pageBitmap.height.toFloat()
                        val screenRatio = availableWidth.value / availableHeight.value
                        val paperWidth: Dp
                        val paperHeight: Dp

                        if (bitmapRatio > screenRatio) {
                            paperWidth = availableWidth
                            paperHeight = availableWidth / bitmapRatio
                        } else {
                            paperHeight = availableHeight
                            paperWidth = availableHeight * bitmapRatio
                        }

                        Box(
                            modifier = Modifier
                                .width(screenWidth * zoomLevel)
                                .height(screenHeight * zoomLevel),
                            contentAlignment = Alignment.Center
                        ) {
                            PDFPageItem(
                                bitmap = pageBitmap,
                                pageNumber = index + 1,
                                width = paperWidth,
                                height = paperHeight,
                                onDoubleTap = {
                                    zoomLevel = if (zoomLevel > 1.5f) 1f else 2.5f
                                }
                            )
                        }
                    }
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(4.dp),
                adapter = rememberScrollbarAdapter(scrollState = currentListState),
            )
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(bottom=4.dp, end=16.dp),
                adapter = rememberScrollbarAdapter(scrollState = horizontalScrollState),
            )
        }
    }
}

@Composable
fun PDFPageItem(
    bitmap: ImageBitmap,
    pageNumber: Int,
    width: Dp,
    height: Dp,
    onDoubleTap: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(2.dp),
        color = Color.White,
        modifier = Modifier
            .size(width, height)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { onDoubleTap() }
                )
            }
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = "Page $pageNumber",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}