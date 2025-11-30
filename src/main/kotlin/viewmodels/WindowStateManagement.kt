package viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WindowStateManagement: ViewModel() {
    private val _isSettingVisible = MutableStateFlow<Boolean>(false);
    val isSettingsVisible: StateFlow<Boolean> = _isSettingVisible

    fun toggleSettingVisible() {
        _isSettingVisible.value = !_isSettingVisible.value;
    }

    companion object {

        @Volatile
        private var instance: WindowStateManagement? = null;

        fun getIntstance(): WindowStateManagement {
            return instance?: synchronized(this) {
                instance?: WindowStateManagement().also { instance = it }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
    }

}