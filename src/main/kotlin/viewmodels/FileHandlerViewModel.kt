package viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import components.FileHandler

class FileHandlerViewModel(): ViewModel() {
    private val fileHandler = FileHandler();

    private val _paths = MutableStateFlow<List<String>>(emptyList());
    val paths: StateFlow<List<String>> = _paths;

    companion object {

        @Volatile
        private var instance: FileHandlerViewModel? = null;

        fun getInstance(): FileHandlerViewModel {
            return instance?: synchronized(this) {
                instance?: FileHandlerViewModel().also { instance = it }
            }
        }
    }

    fun getPaths() {
        viewModelScope.launch {
            _paths.value = fileHandler.allDirectories;
        }
    }

    fun addPath(path: String) {
        viewModelScope.launch {
            fileHandler.addDirectory(path);
            getPaths();
        }
    }
}