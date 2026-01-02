package viewmodels

import components.PDF
import components.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.image.BufferedImage
import java.nio.file.Path

class TabBarViewModel : ViewModel() {
    // Manages all the tabs opened
    private var _tabList = MutableStateFlow<MutableList<Tab>>(emptyList<Tab>().toMutableList())
    var tabList = _tabList.asStateFlow()

    // Added this so that UI can know which tab is active
    private var _curActiveTab = MutableStateFlow<Int>(0)
    var curActiveTab = _curActiveTab.asStateFlow()

    // Added this so that TabScreen can see whether pdf is open in the current file
    private var _isLoaded = MutableStateFlow<Boolean>(false)
    var isLoaded = _isLoaded.asStateFlow()

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
        _curActiveTab.value = _tabList.value.size - 1
    }

    fun chooseTab(tabNumber: Int) {
        _curActiveTab.value = tabNumber
    }

    fun closeTab(tabNumber: Int) {
        if (tabNumber == 0) {}
        _tabList.value.removeAt(tabNumber)
        _curActiveTab.value--
    }

    fun openPDF() {
        val tab = _tabList.value.get(_curActiveTab.value);
        tab.setIsLoaded();
    }

    fun getIsTabLoaded(): Boolean {
        return _tabList.value.get(_curActiveTab.value).getIsLoaded()
    }

    fun getPDFImage(pageNo: Int): BufferedImage? {
        return try {
            _tabList.value.get(_curActiveTab.value).loadImage(pageNo)
        } catch (e: Exception) {
            println("faced an exception during loading image number $pageNo. fun getPDFImage of TabBarViewModel")
            null
        }
    }
}