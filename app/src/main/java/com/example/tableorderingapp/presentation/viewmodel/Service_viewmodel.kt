package com.example.tableorderingapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.repository.NetworkRepositoryImpl
import com.example.tableorderingapp.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Service_viewmodel@Inject constructor (private val networkRepositoryImpl: NetworkRepositoryImpl) : ViewModel() {

    private var _allmenu:MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val allmenu:LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _allmenu

    init {
        getAllMenu()
    }

    fun getAllMenu(){
        viewModelScope.launch(Dispatchers.IO) {
            networkRepositoryImpl.getAllMenu()
                .onEach {
                    _allmenu.value=it
                }
                .launchIn(viewModelScope)
        }
    }
}