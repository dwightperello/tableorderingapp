package com.example.tableorderingapp.domain.repository

import com.example.tableorderingapp.domain.model.request.NeedAssistance
import com.example.tableorderingapp.domain.model.request.PostTableOrder
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.model.response.AssistanceStatusResponse
import com.example.tableorderingapp.domain.network.NetworkService
import com.example.tableorderingapp.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody

class NetworkRepositoryImpl(private val networkService: NetworkService):NetworkRepository {
    override suspend fun getAllMenu(): Flow<ResultState<ArrayList<AllMenuModelItem>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = networkService.getAllMenu()
            emit(ResultState.Success(response))

        }
        catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override suspend fun CallAssistance(
        tablenumber: Int,
        needAssistance: NeedAssistance
    ): Flow<ResultState<ResponseBody>> = flow{
        emit(ResultState.Loading)
        try {
            val response = networkService.CallAssistance(tablenumber,needAssistance)
            emit(ResultState.Success(response))

        }
        catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override suspend fun getAssistanceStatus(tablenumber: Int): Flow<ResultState<AssistanceStatusResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = networkService.getAssistanceStatus(tablenumber)
            emit(ResultState.Success(response))

        }
        catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override suspend fun postAllOrders(postTableOrder: PostTableOrder): Flow<ResultState<ResponseBody>> = flow {
        emit(ResultState.Loading)
        try {
            val response = networkService.postAllOrder(postTableOrder)
            emit(ResultState.Success(response))

        }
        catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override suspend fun getTotalAmountTable(tablenumber: Int): Flow<ResultState<PostTableOrder>> = flow {
        emit(ResultState.Loading)
        try {
            val response = networkService.getAmountOrOrders(tablenumber)
            emit(ResultState.Success(response))

        }
        catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }
}