package com.example.tableorderingapp.domain.converter

import com.example.tableorderingapp.domain.model.request.NeedAssistance


object ConvertNeedAssistance {
    val updateAssistance:(Boolean)-> NeedAssistance ={
        var assistance = NeedAssistance(
            needAssistance = it
        )
        assistance
    }
}