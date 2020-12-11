package com.isuo.inspection.application.model.bean

import android.os.Parcelable


data class HomePageBean(
    val dataCount: Int,
    val equipmentCount: Int,
    val substationCount: Int,
    val substationList: List<SubstationNetBean>
)

class SubstationNetBean(
    val address: String,
    val createTime: Long,
    val enterpriseId: Int,
    val enterpriseName: String,
    val installedCapacity: String,
    val lastModifyTime: Long,
    val latitude: Double,
    val longitude: Double,
    val operatingCapacity: String,
    val principal: String,
    val remark: String,
    val runDate: Long,
    val status: Int,
    val substationId: Long,
    val substationName: String,
    val substationTypeCode: Int,
    val voltageRank: Int
)

data class EquipmentNetBean(
    val equipmentId: Long,
    val equipmentName: String,
    val serialNumber: String?,
    val equipmentTypeCode: Int,
    val equipmentTypeName: String,
    val manufacturer: String,
    val runDate: Long,
    val status: Int,
    val substationId: Int,
    val substationName: String,
    val voltageRank: Int
)

data class MeasuringBean(
    val list: List<MeasuringListBean>,
    val totalCount: Int
)

data class MeasuringListBean(
    val checkTypeId: Int,
    val checkedStatus: Boolean,
    val createTime: Long,
    val equipmentTypeId: Int,
    val isDel: Int,
    val measuringPointId: Long,
    val measuringPointName: String,
    val pointTypeId: String,
    val state: Int
)


