package com.example.tableorderingapp.domain.network

import com.example.tableorderingapp.domain.model.request.NeedAssistance
import com.example.tableorderingapp.domain.model.request.PostTableOrder
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.model.response.AssistanceStatusResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {
    @GET("Menus")
    suspend fun getAllMenu(): ArrayList<AllMenuModelItem>

    @POST("TableOrder")
    suspend fun postAllOrder(@Body postTableOrder: PostTableOrder) : Response<ResponseBody>

    @GET("TableOrder/ordertableamount/{tablenumber}")
    suspend fun getAmountOrOrders(@Path("tablenumber")tablenumber:Int): Response<PostTableOrder>

    @GET("TableAssistance/GetAssistanceStatus")
    suspend fun getAssistanceStatus(@Query("tablenumber")tablenumber:Int): Response<AssistanceStatusResponse>

    @PUT("TableAssistance/{tablenumber}")
    suspend fun CallAssistance(@Path("tablenumber") tablenumber: Int, @Body needAssistance: NeedAssistance): ResponseBody
}
