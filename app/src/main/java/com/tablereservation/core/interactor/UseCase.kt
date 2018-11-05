package com.tablereservation.core.interactor

import com.tablereservation.core.functional.Either
import com.tablereservation.core.exception.Failure
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params> : CoroutineScope where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    override val coroutineContext = Job() + Dispatchers.Main

    private var contextForBackgroundWork : CoroutineContext = Dispatchers.IO

    /**
     * Warning: this is a hacky method that MUST NOT be used outside of Unit test, its specific
     * for making sure that coroutines are executed properly in test environment.
     */
    fun setupForUnitTests(){
        contextForBackgroundWork = Dispatchers.Main
    }

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}): Job {
         return launch {
            val result = withContext(contextForBackgroundWork) { run(params) }
            onResult(result)
        }
    }

    class None
}
