package ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.Tab
import viewmodels.ViewModelProviderImpl
import viewmodels.WindowStateManagement

@Composable
fun tabBar(
    viewModelProviderImpl: ViewModelProviderImpl,
    windowStateManagement: WindowStateManagement
) {
    val tabList = viewModelProviderImpl.getTabBarViewModel().tabList.collectAsState().value
    val activeTabNumber = viewModelProviderImpl.getTabBarViewModel().curActiveTab.collectAsState().value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF202124)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.Bottom
        ) {
            items(tabList) { tab ->
                ChromeTabItem(
                    tab = tab,
                    isActive = activeTabNumber == tabList.indexOf(tab),
                    onCloseClick = {
                       viewModelProviderImpl.getTabBarViewModel().closeTab(tabList.indexOf(tab))
                    },
                    onTabClick = {
                        viewModelProviderImpl.getTabBarViewModel().chooseTab(tabList.indexOf(tab))
                    }
                )
            }
            item() {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Close Tab",
                    tint = Color(0xFFAAAAAA),
                    modifier = Modifier
                        .size(40.dp)
                        .fillMaxHeight()
                        .padding(vertical = 12.dp)
                        .clickable { viewModelProviderImpl.getTabBarViewModel().addNewTab() },
                )
            }
        }

        IconButton(
            onClick = {
                windowStateManagement.toggleSettingVisible()
            },
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
        ) {
            Image(
                imageVector = Icons.Default.Settings,
                contentDescription = "settings",
                colorFilter = ColorFilter.tint(Color.White),
            )
        }
    }
}

@Composable
fun ChromeTabItem(
    tab: Tab,
    isActive: Boolean,
    onCloseClick: () -> Unit,
    onTabClick: () -> Unit
) {
    val backgroundColor = if (isActive) Color(0xFF323639) else Color.Transparent
    val contentColor = if (isActive) Color.White else Color(0xFFAAAAAA)

    val tabShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    Row(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight(0.9f)
            .background(color = backgroundColor, shape = tabShape)
            .clickable { onTabClick() }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = tab.tabName,
            color = contentColor,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close Tab",
            tint = contentColor,
            modifier = Modifier
                .size(16.dp)
                .clickable { onCloseClick() }
        )
    }

    if (!isActive) {
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .height(32.dp)
                .background(Color.Gray)
        )
    }
}