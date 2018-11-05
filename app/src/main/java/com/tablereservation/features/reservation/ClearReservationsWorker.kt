package com.tablereservation.features.reservation

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tablereservation.TTL_RESERVATIONS_IN_MINUTES
import com.tablereservation.AndroidApplication
import com.tablereservation.core.interactor.UseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ClearReservationsWorker(context : Context, params : WorkerParameters) : Worker(context, params) {

    @Inject
    lateinit var clearReservationsUseCase: ClearReservationsUseCase

    override fun doWork(): Result {
        if (applicationContext is AndroidApplication) {
            (applicationContext as AndroidApplication).appComponent.inject(this)
        }
        clearReservationsUseCase(UseCase.None())
        return Result.SUCCESS
    }
}

// This is a bit hackish since there is no initial delay for period work (at least in alpha10)
class DelegateWorker(context : Context, params : WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val deleteWorkBuilder =
            PeriodicWorkRequestBuilder<ClearReservationsWorker>(TTL_RESERVATIONS_IN_MINUTES, TimeUnit.MINUTES)
        val reservationsDeleteWork = deleteWorkBuilder.build()
        WorkManager.getInstance().enqueue(reservationsDeleteWork)
        return Result.SUCCESS
    }
}