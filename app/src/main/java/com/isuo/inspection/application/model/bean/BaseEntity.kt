package com.isuo.inspection.application.model.bean

class BaseEntity<T>(var errorCode: Int, var message: String, var data: T)