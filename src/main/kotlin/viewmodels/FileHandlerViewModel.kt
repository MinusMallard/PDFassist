package viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import components.FileHandler
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path

class FileHandlerViewModel: ViewModel() {
    private val fileHandler = FileHandler()

    private var _paths = MutableStateFlow<List<String>>(emptyList())
    var paths: StateFlow<List<String>> = _paths.asStateFlow()

    private var _files = MutableStateFlow<List<Path>>(emptyList())
    var files: StateFlow<List<Path>> = _files.asStateFlow()

    companion object {

        @Volatile
        private var instance: FileHandlerViewModel? = null

        fun getInstance(): FileHandlerViewModel {
            return instance?: synchronized(this) {
                instance?: FileHandlerViewModel().also { instance = it }
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            fileHandler.allDirectories
            fileHandler.searchInPaths()
            getPaths()
            getFiles()
        }
    }

    private fun getPaths() {
            _paths.value = fileHandler.allDirectories
    }

    fun getFiles() {
        _files.value = fileHandler.searchInPaths()
    }

    fun addPath(path: String) {
        viewModelScope.launch {
            fileHandler.addDirectory(path)
            getPaths()
        }
    }
}