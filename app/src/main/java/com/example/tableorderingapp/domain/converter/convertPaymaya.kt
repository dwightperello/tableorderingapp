package com.example.tableorderingapp.domain.converter

import com.example.tableorderingapp.util.GlobalVariable
import com.paymaya.sdk.android.checkout.models.CheckoutRequest
import com.paymaya.sdk.android.checkout.models.Item
import com.paymaya.sdk.android.checkout.models.ItemAmount
import com.paymaya.sdk.android.common.models.RedirectUrl
import com.paymaya.sdk.android.common.models.TotalAmount

object convertPaymaya {

    val BuildPaymayaCheckoutRequest:()-> CheckoutRequest={
        var cc= CheckoutRequest(
            totalAmount = converttotalamount(),
            buyer = null,
            items= convertitems(),
            requestReferenceNumber = GlobalVariable.cCpaymentOrderDetails!!.refnumber.toString(),
            redirectUrl = redirecturl()
        )
        cc
    }

    val converttotalamount:() -> TotalAmount ={
        var amount= GlobalVariable.cCpaymentOrderDetails!!.total
        var meth= TotalAmount(
            value=amount.toBigDecimal(),
            currency="PHP",
            details = null
        )
        meth
    }

    val converttotalamount2:(Double) -> TotalAmount ={

        var meth= TotalAmount(
            value=it.toBigDecimal(),
            currency="PHP",
            details = null
        )
        meth
    }


    val convertitems:()->List<Item> ={
        GlobalVariable.cCpaymentOrderDetails!!.creditcardorderdetails.forEach {
            var meth= Item(
                name=it.orderTitle,
                quantity = it.qty,
                code= it.orderTableTag.toString(),
                description = null,
                amount = convertitemamount(it.price,null),
                totalAmount = converttotalamount2(it.price)

            )
            GlobalVariable.Orderdetailsspaymaya!!.add(meth)
        }
        GlobalVariable.Orderdetailsspaymaya!!.toList()

    }



    val convertitemamount:(Double,String?) -> ItemAmount ={price,details->
        var meth= ItemAmount(
            value= price!!.toBigDecimal(),
            details= null
        )
        meth
    }
    val redirecturl:()-> RedirectUrl ={
        var meth= RedirectUrl(
            success= "http://success.com",
            failure= "http://failure.com",
            cancel="http://cancel.com"
        )
        meth
    }
}