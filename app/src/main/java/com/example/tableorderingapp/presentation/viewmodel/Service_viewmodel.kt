package com.example.tableorderingapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tableorderingapp.domain.model.request.NeedAssistance
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.model.response.AssistanceStatusResponse
import com.example.tableorderingapp.domain.repository.NetworkRepositoryImpl
import com.example.tableorderingapp.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class Service_viewmodel@Inject constructor (private val networkRepositoryImpl: NetworkRepositoryImpl) : ViewModel() {

    private var _allmenu:MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val allmenu:LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _allmenu

    private var _callassistance:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val callassistance :LiveData<ResultState<ResponseBody>> get() = _callassistance

    private var _assistancestatus :MutableLiveData<ResultState< AssistanceStatusResponse>> = MutableLiveData()
    val assistancestatus:LiveData<ResultState<AssistanceStatusResponse>> get() = _assistancestatus
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

    fun callAssistance(tablenumber:Int,needAssistance: NeedAssistance){
        viewModelScope.launch(Dispatchers.IO) {
            networkRepositoryImpl.CallAssistance(tablenumber,needAssistance)
                .onEach {
                    _callassistance.value=it
                }
                .launchIn(viewModelScope)
        }
    }

    fun getAssistanceStatus(tablenumber: Int){
        viewModelScope.launch(Dispatchers.IO) {
            networkRepositoryImpl.getAssistanceStatus(tablenumber)
                .onEach {
                    _assistancestatus.value=it
                }
                .launchIn(viewModelScope)
        }
    }
}