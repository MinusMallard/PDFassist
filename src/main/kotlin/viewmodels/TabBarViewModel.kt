package viewmodels

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import components.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path

class TabBarViewModel : ViewModel() {
    // Manages all the tabs opened
    private var _tabList = MutableStateFlow(emptyList<Tab>().toMutableList())
    var tabList = _tabList.asStateFlow()

    private var _lazyListStates = MutableStateFlow(emptyList<LazyListState>().toMutableList())
    var lazyListState = _lazyListStates.asStateFlow()

    // Added this so that UI can know which tab is active
    private var _curActiveTab = MutableStateFlow(0)
    var curActiveTab = _curActiveTab.asStateFlow()

    private var _isLoaded = MutableStateFlow(false)
    var isLoaded = _isLoaded.asStateFlow()

    /**This HashMap is used for storing all the cached images for current tab**/
    private var _bitmaps = MutableStateFlow(emptyList<ImageBitmap>().toMutableList())
    var bitmaps = _bitmaps.asStateFlow()

    /**This variable tracks the total number of pages in a given pdf**/
    private var _totalPages = MutableStateFlow(0);
    var totalPages = _totalPages.asStateFlow()

    private var _tabName = MutableStateFlow(emptyList<String>().toMutableList());
    var tabName = _tabName.asStateFlow()

    /**This is like static in java I have created a instance which is visible from
    every thread and is thread safe which is used to access the instance of the class**/
    companion object {
        @Volatile
        private var instance: TabBarViewModel? = null

        fun getInstance(): TabBarViewModel {
            return instance?: synchronized(this) {
                instance?: TabBarViewModel().also { instance = it }
            }
        }
    }

    fun addNewTab() {
        _tabList.value.addLast(Tab())
        _lazyListStates.value.addLast(LazyListState())
        _curActiveTab.value = _tabList.value.size - 1
        chooseTab(_curActiveTab.value)
    }

    fun chooseTab(tabNumber: Int) {
        _curActiveTab.value = tabNumber
        updateIsLoaded()
        if (_isLoaded.value) {
            setTotalPages()
        }
        setTabName()
        getPDFImageBitmap()
    }

    fun closeTab(tabNumber: Int) {
        println("Tab Number is :::::::::::::::::::::::::::::::::::::::: $tabNumber")
        _tabList.value.removeAt(tabNumber)
        _lazyListStates.value.removeAt(tabNumber)
        _curActiveTab.value -= 1
        if (_tabList.value.size > 0) {
            getPDFImageBitmap()
        }
        _tabName.value.removeAt(tabNumber)

    }

    fun openPDF(path: Path) {
        val tab = _tabList.value[_curActiveTab.value];
        tab.setPath(path)
        setTotalPages()
        updateIsLoaded()
        getPDFImageBitmap()
        setTabName()
    }
    private fun getPDFImageBitmap() {
        _bitmaps.value = emptyList<ImageBitmap>().toMutableList()
        viewModelScope.launch {
            val list = _tabList.value[_curActiveTab.value].loadImage()
            _bitmaps.value = list.map { it.toComposeImageBitmap() }.toMutableList();
        }
    }

    private fun updateIsLoaded() {
        _isLoaded.value =
            _tabList.value
                .getOrNull(_curActiveTab.value)
                ?.getIsLoaded() == true
    }

    private fun setTotalPages() {
        _totalPages.value = _tabList.value[_curActiveTab.value].totalPages
    }

    private fun setTabName() {
        _tabName.value = emptyList<String>().toMutableList()
        _tabName.value = _tabList.value.map { it.tabName }.toMutableList()
    }
}