package com.example.tableorderingapp.domain.converter

import com.example.tableorderingapp.domain.model.request.updateCCpayment

object convertUpdateccpayment {
    val updateccpayment:(Int,Boolean)-> updateCCpayment={id,paid->
        var method= updateCCpayment(
            id = id,
            isPaid = paid
        )
        method
    }
}