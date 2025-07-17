package com.example.sharktracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SharkTrackingViewModel : ViewModel() {
    private val repository = SharkRepository()

    private val _sharkPings = MutableLiveData<List<SharkPing>>()
    val sharkPings: LiveData<List<SharkPing>> = _sharkPings

    private val _sharks = MutableLiveData<List<Shark>>()
    val sharks: LiveData<List<Shark>> = _sharks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var isAutoRefreshEnabled = true

    init {
        loadInitialData()
        startAutoRefresh()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true

            // Load sharks first
            repository.getSharks().fold(
                onSuccess = { _sharks.value = it },
                onFailure = { _error.value = "Failed to load sharks: ${it.message}" }
            )

            // Then load shark pings (locations)
            loadSharkPings()
        }
    }

    private fun loadSharkPings() {
        viewModelScope.launch {
            repository.getSharkPings().fold(
                onSuccess = {
                    _sharkPings.value = it
                    _isLoading.value = false
                    _error.value = null
                },
                onFailure = {
                    _error.value = "Failed to load shark locations: ${it.message}"
                    _isLoading.value = false
                }
            )
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (isAutoRefreshEnabled) {
                delay(30000) // Refresh every 30 seconds
                if (isAutoRefreshEnabled) {
                    loadSharkPings()
                }
            }
        }
    }

    fun refreshData() {
        loadInitialData()
    }

    fun toggleAutoRefresh() {
        isAutoRefreshEnabled = !isAutoRefreshEnabled
        if (isAutoRefreshEnabled) {
            startAutoRefresh()
        }
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        isAutoRefreshEnabled = false
    }
}