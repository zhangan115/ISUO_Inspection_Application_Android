/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.isuo.inspection.application.base.factory

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.isuo.inspection.application.repository.DataRepository
import com.isuo.inspection.application.repository.TaskRepository
import com.isuo.inspection.application.repository.UserRepository
import com.isuo.inspection.application.ui.login.LoginViewModel
import com.isuo.inspection.application.ui.login.forget_pass.ForgetPassViewModel
import com.isuo.inspection.application.ui.main.MainViewModel
import com.isuo.inspection.application.ui.splash.SplashViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
    private val dataRepository: DataRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(userRepository)
            isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel(userRepository)
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(userRepository,taskRepository)
            isAssignableFrom(ForgetPassViewModel::class.java) ->
                ForgetPassViewModel(userRepository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
