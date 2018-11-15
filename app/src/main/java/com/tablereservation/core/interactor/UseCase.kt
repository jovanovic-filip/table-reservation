package com.tablereservation.core.interactor

import com.tablereservation.core.functional.Either
import com.tablereservation.core.exception.Failure
import kotlinx.coroutines.*
import javax.inject.Inject
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

    override lateinit var coroutineContext : CoroutineContext

    @Inject lateinit var contextProvider : UseCaseContextProvider

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}): Job {
        coroutineContext = contextProvider.main
         return launch { /*coroutineContext + contextProvider.main*/
            val result = withContext(contextProvider.io) { run(params) }
            onResult(result)
        }
    }
    class None

    fun cancel(){
        if(this::coroutineContext.isInitialized){
            coroutineContext.cancel()
        }
    }
}
