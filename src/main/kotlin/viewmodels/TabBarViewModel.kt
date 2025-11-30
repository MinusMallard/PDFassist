package viewmodels

import components.PDF
import components.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path

class TabBarViewModel : ViewModel() {
    private var _tabList = MutableStateFlow<MutableList<Tab>>(emptyList<Tab>().toMutableList())
    var tabList = _tabList.asStateFlow()

    private var _curActiveTab = MutableStateFlow<Int>(0)
    var curActiveTab = _curActiveTab.asStateFlow()

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
}