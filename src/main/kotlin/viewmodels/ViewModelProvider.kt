package viewmodels

class ViewModelProviderImpl {
    fun getWindowsStateManagement(): WindowStateManagement {
        return WindowStateManagement.getIntstance();
    }

    fun getFileHandlerViewModel(): FileHandlerViewModel {
        return FileHandlerViewModel.getInstance();
    }

    fun getTabBarViewModel(): TabBarViewModel {
        return TabBarViewModel.getInstance();
    }
}

