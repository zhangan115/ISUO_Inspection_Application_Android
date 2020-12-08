package com.isuo.inspection.application.model.bean

class MessageChartEvent {
    var message: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var positionId: Int? = null

    fun MessageChartEvent(message: String?, startTime: String?, endTime: String?, positionId: Int?) {
        this.message = message
        this.startTime = startTime
        this.endTime = endTime
        this.positionId = positionId
    }
}