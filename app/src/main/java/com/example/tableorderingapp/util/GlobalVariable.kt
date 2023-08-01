package com.example.tableorderingapp.util

import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.domain.model.response.CCpaymentOrderDetails
import com.example.tableorderingapp.domain.model.response.Submenu
import com.paymaya.sdk.android.checkout.models.Item

object GlobalVariable {
    const val API_BASE_URL="http://10.0.0.9:8083/api/"

    var itemToaddToCart: Submenu?= null

    var orderdetail:MutableList<Orderdetail>? = mutableListOf()

    var tablenumber:Int?= null

    var recomendedItem:ArrayList<Submenu>?= ArrayList<Submenu>()

    var cCpaymentOrderDetails:CCpaymentOrderDetails?= null

    var Orderdetailsspaymaya:MutableList<Item>?= mutableListOf()
}