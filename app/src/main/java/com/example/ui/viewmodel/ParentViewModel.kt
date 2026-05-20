package com.example.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ParentEntity
import com.example.data.ParentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SearchType {
    STUDENT_NAME, JOB, STUDENT_CLASS
}

class ParentViewModel(private val repository: ParentRepository) : ViewModel() {

    // Login state indicators
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)

    // Form parameter captures
    var studentName by mutableStateOf("")
    var studentClass by mutableStateOf("")
    var parentName by mutableStateOf("")
    var job by mutableStateOf("")
    var support by mutableStateOf("")
    var phone by mutableStateOf("")
    var address by mutableStateOf("")

    // List searching parameters
    var searchQuery by mutableStateOf("")
    var searchType by mutableStateOf(SearchType.STUDENT_NAME)

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    // Full Local Parents Flow
    val parents: StateFlow<List<ParentEntity>> = repository.allParents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Live search updates combining UI states and repository updates
    val filteredParents: StateFlow<List<ParentEntity>> = combine(
        repository.allParents,
        snapshotFlow { searchQuery },
        snapshotFlow { searchType }
    ) { parentList, query, type ->
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) {
            parentList
        } else {
            parentList.filter {
                val valueToMatch = when (type) {
                    SearchType.STUDENT_NAME -> it.studentName
                    SearchType.JOB -> it.job
                    SearchType.STUDENT_CLASS -> it.studentClass
                }
                valueToMatch.contains(trimmedQuery, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Initial silent background pull
        refreshCloud()
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun checkLogin() {
        if (username.trim() == "vidyartha" && password == "19460101") {
            isLoggedIn = true
            loginError = null
            username = ""
            password = ""
        } else {
            loginError = "Incorrect password!"
        }
    }

    fun logout() {
        isLoggedIn = false
        clearForm()
        searchQuery = ""
    }

    fun clearForm() {
        studentName = ""
        studentClass = ""
        parentName = ""
        job = ""
        support = ""
        phone = ""
        address = ""
    }

    fun refreshCloud() {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = repository.fetchFromCloud()
            _isSyncing.value = false
            if (result.isSuccess) {
                _toastMessage.value = "Successfully synced with the cloud!"
            } else {
                _toastMessage.value = "Sync failed (local database cache is up-to-date)"
            }
        }
    }

    fun saveParentData() {
        val sName = studentName.trim()
        val sClass = studentClass.trim()
        val pName = parentName.trim()
        val j = job.trim()
        val sup = support.trim()
        val ph = phone.trim()
        val addr = address.trim()

        if (sName.isEmpty() || ph.isEmpty()) {
            _toastMessage.value = "Student Name and Contact Phone are required!"
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            val result = repository.saveParent(
                studentName = sName,
                studentClass = sClass,
                parentName = pName,
                job = j,
                support = sup,
                phone = ph,
                address = addr
            )
            _isSaving.value = false
            if (result.isSuccess) {
                _toastMessage.value = "Information successfully saved!"
                clearForm()
            } else {
                val errorMsg = result.exceptionOrNull()?.message
                if (errorMsg == "EXISTS") {
                    _toastMessage.value = "This registration record already exists!"
                } else {
                    _toastMessage.value = "Error saving: ${result.exceptionOrNull()?.localizedMessage}"
                }
            }
        }
    }

    fun deleteEntry(id: String) {
        viewModelScope.launch {
            val result = repository.deleteParent(id)
            if (result.isSuccess) {
                _toastMessage.value = "Record successfully deleted!"
            } else {
                _toastMessage.value = "Delete failed: ${result.exceptionOrNull()?.localizedMessage}"
            }
        }
    }

    class Factory(private val rawRepo: ParentRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ParentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ParentViewModel(rawRepo) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
