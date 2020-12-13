package com.isuo.inspection.application.model.bean

class MessageEvent {
    var message: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var positionId: Long? = null
    var positionName: String? = null

    fun MessageEvent(message: String?, startTime: String?, endTime: String?, positionId: Long?,positionName:String?) {
        this.message = message
        this.startTime = startTime
        this.endTime = endTime
        this.positionId = positionId
        this.positionName = positionName
    }
}