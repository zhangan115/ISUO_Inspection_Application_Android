package com.isuo.inspection.application.model.bean

data class AppVersion(
    var versionDescription: String,
    var isUpgrade: Int,
    var url: String,
    var newVersion: Int
)