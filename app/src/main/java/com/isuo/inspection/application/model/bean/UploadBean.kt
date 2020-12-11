package com.isuo.inspection.application.model.bean

class UploadBean {
    var dataInfo: ArrayList<UploadDataInfo>? = null
}

class UploadDataInfo {
    var dataType: Int? = null
    var substationId: Long? = null
    var substationName: String? = null
    var equipmentId: Long? = null
    var equipmentName: String? = null
    var positionId: Long? = null
    var positionName: String? = null
    var peakValue: String? = null
    var backgroundPeakValue: String? = null
    var frequencyComponent1: String? = null
    var frequencyComponent2: String? = null
    var picNode: String? = null
    var ultrasounId: Long = 0L
}