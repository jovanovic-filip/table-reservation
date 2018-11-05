package com.tablereservation

import android.app.Application
import com.tablereservation.core.di.ApplicationComponent
import com.tablereservation.core.di.ApplicationModule
import com.tablereservation.core.di.DaggerApplicationComponent
import com.tablereservation.core.di.RepositoryModule

class AndroidApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .repositoryModule(RepositoryModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()

    }

    private fun injectMembers() = appComponent.inject(this)

}
