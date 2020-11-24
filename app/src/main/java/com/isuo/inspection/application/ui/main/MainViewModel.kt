package com.isuo.inspection.application.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.repository.UserRepository

class MainViewModel(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    var toastStr: MutableLiveData<String> = MutableLiveData()

}