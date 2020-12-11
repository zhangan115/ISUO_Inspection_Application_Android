package com.isuo.inspection.application.model.bean

class MessageEvent {
    var message: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var positionId: Long? = null

    fun MessageEvent(message: String?, startTime: String?, endTime: String?, positionId: Long?) {
        this.message = message
        this.startTime = startTime
        this.endTime = endTime
        this.positionId = positionId
    }
}