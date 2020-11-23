package com.isuo.inspection.application.ui.splash

import androidx.lifecycle.ViewModel
import com.isuo.inspection.application.repository.UserRepository

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

import java.util.concurrent.TimeUnit

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val delayTime = 2L

    fun start(): Single<Int> {
        return arrayOf(userRepository.isLogin()).toObservable()
            .delay(delayTime, TimeUnit.SECONDS).single(0)
    }

}