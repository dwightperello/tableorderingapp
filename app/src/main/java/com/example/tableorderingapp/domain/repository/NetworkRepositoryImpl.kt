package com.example.tableorderingapp.domain.repository

import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.network.NetworkService
import com.example.tableorderingapp.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
}