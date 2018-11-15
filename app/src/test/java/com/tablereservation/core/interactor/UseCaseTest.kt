package com.tablereservation.core.interactor

import com.tablereservation.UnitTest
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class UseCaseTest : UnitTest() {

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    private val useCase = DemoUseCase()

    @Before fun setup(){
           useCase.contextProvider = TestUseCaseContextProvider()
    }

    @Test fun `running use case should return 'Either' of use case type`() {
        val params = DemoParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }
        result shouldEqual Either.Right(DemoType(TYPE_TEST))
    }

    @Test fun `running use case should return correct data`() {
        var useCaseResult : Either<Failure, DemoType>? = null
        val params = DemoParams(TYPE_PARAM)
        val onResult = { result: Either<Failure, DemoType> -> useCaseResult = result }
        runBlocking { useCase(params, onResult).join() }
        useCaseResult shouldEqual Either.Right(DemoType(TYPE_TEST))
    }

    data class DemoType(val name: String)
    data class DemoParams(val name: String)

    private inner class DemoUseCase : UseCase<DemoType, DemoParams>() {
        override suspend fun run(params: DemoParams): Either.Right<DemoType> {
            return Either.Right(DemoType(TYPE_TEST))
        }
    }
}
