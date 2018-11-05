package com.tablereservation.core.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import com.tablereservation.core.platform.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

inline fun <reified T : ViewModel> androidx.fragment.app.Fragment.viewModel(factory: Factory, body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory).get(T::class.java)
    vm.body()
    return vm
}

val BaseFragment.appContext: Context get() = activity?.applicationContext!!


fun BaseFragment.showSnackbar(stringId : Int) {
    activity?.coordinatorLayout?.let {
        Snackbar.make(
            it, stringId,
        Snackbar.LENGTH_SHORT)
        .show()
    }
}