package viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

open class ViewModel {

    open val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    open fun onCleared() {
        viewModelScope.cancel()
    }
}