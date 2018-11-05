package com.tablereservation.core.platform

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tablereservation.R
import com.tablereservation.TTL_RESERVATIONS_IN_MINUTES
import com.tablereservation.AndroidApplication
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.features.reservation.ClearReservationsUseCase
import com.tablereservation.features.reservation.DelegateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var clearReservationsUseCase: ClearReservationsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.app_toolbar))
        (application as AndroidApplication).appComponent.inject(this)
        setupReservationsDeletion()
    }

    private fun setupReservationsDeletion() {
        val delayedPeriodicWorkRequest = OneTimeWorkRequestBuilder<DelegateWorker>()
            .setInitialDelay(TTL_RESERVATIONS_IN_MINUTES, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance().enqueue(delayedPeriodicWorkRequest)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_clear_reservations -> {
            clearReservationsUseCase(UseCase.None())
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }
}