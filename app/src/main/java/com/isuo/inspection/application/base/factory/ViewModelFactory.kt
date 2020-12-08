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
import com.isuo.inspection.application.ui.data.DataBaseViewModel
import com.isuo.inspection.application.ui.data.chart.ChartViewModel
import com.isuo.inspection.application.ui.data.filter.DataFilterViewModel
import com.isuo.inspection.application.ui.data.history.HistoryListViewModel
import com.isuo.inspection.application.ui.login.LoginViewModel
import com.isuo.inspection.application.ui.user.user_info.UserInfoViewModel
import com.isuo.inspection.application.ui.main.MainViewModel
import com.isuo.inspection.application.ui.main.check_item.SubCheckItemViewMode
import com.isuo.inspection.application.ui.main.device_list.DeviceListViewModel
import com.isuo.inspection.application.ui.main.input.InputViewModel
import com.isuo.inspection.application.ui.main.search.SearchSubViewModel
import com.isuo.inspection.application.ui.splash.SplashViewModel
import com.isuo.inspection.application.ui.user.forget_pass.ForgetPassViewModel
import com.isuo.inspection.application.ui.user.suggest.SuggestViewModel
import com.isuo.inspection.application.ui.user.user_center.UserCenterViewModel

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
                MainViewModel(userRepository, taskRepository)
            isAssignableFrom(ForgetPassViewModel::class.java) ->
                ForgetPassViewModel(userRepository)
            isAssignableFrom(UserInfoViewModel::class.java) ->
                UserInfoViewModel(userRepository)
            isAssignableFrom(UserCenterViewModel::class.java) ->
                UserCenterViewModel(userRepository)
            isAssignableFrom(SearchSubViewModel::class.java) ->
                SearchSubViewModel(taskRepository)
            isAssignableFrom(SubCheckItemViewMode::class.java) ->
                SubCheckItemViewMode(taskRepository)
            isAssignableFrom(DeviceListViewModel::class.java) ->
                DeviceListViewModel(taskRepository)
            isAssignableFrom(InputViewModel::class.java) ->
                InputViewModel(taskRepository)
            isAssignableFrom(DataBaseViewModel::class.java) ->
                DataBaseViewModel(dataRepository)
            isAssignableFrom(DataFilterViewModel::class.java) ->
                DataFilterViewModel()
            isAssignableFrom(ChartViewModel::class.java) ->
                ChartViewModel(taskRepository)
            isAssignableFrom(HistoryListViewModel::class.java) ->
                HistoryListViewModel(taskRepository)
            isAssignableFrom(SuggestViewModel::class.java) ->
                SuggestViewModel(userRepository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
