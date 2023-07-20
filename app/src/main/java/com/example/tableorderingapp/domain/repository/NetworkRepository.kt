package com.example.tableorderingapp.domain.repository

import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.util.ResultState
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun getAllMenu() : Flow<ResultState<ArrayList<AllMenuModelItem>>>


}