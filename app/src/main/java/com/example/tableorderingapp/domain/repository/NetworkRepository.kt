package com.example.tableorderingapp.domain.repository

import com.example.tableorderingapp.domain.model.request.NeedAssistance
import com.example.tableorderingapp.domain.model.request.PostTableOrder
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.model.response.AssistanceStatusResponse
import com.example.tableorderingapp.util.ResultState
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface NetworkRepository {
    suspend fun getAllMenu() : Flow<ResultState<ArrayList<AllMenuModelItem>>>

    suspend fun CallAssistance(tablenumber:Int,needAssistance: NeedAssistance):Flow<ResultState<ResponseBody>>
    suspend fun getAssistanceStatus(tablenumber: Int):Flow<ResultState<AssistanceStatusResponse>>

    suspend fun postAllOrders(postTableOrder: PostTableOrder):Flow<ResultState<ResponseBody>>
}