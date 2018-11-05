package com.tablereservation.core.platform

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tablereservation.AndroidApplication
import com.tablereservation.core.di.ApplicationComponent
import com.tablereservation.core.extension.invisible
import com.tablereservation.core.extension.visible
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : androidx.fragment.app.Fragment() {

    abstract fun layoutId(): Int

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layoutId(), container, false)


    internal fun showProgress() = activity?.progressBar?.visible()

    internal fun hideProgress() = activity?.progressBar?.invisible()
}
