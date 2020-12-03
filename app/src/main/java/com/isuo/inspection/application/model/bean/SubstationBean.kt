package com.isuo.inspection.application.model.bean

import androidx.databinding.ObservableField

class SubstationBean(id: Long, name: String) {
    val id: ObservableField<Long> = ObservableField(id)
    val name: ObservableField<String> = ObservableField(name)
}