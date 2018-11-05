package com.tablereservation.core.interactor

import com.tablereservation.AndroidTest
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class UseCaseTest : AndroidTest() {

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    private val useCase = MyUseCase()

    @Before fun setup(){
        useCase.setupForUnitTests()
    }

    @Test fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }
        result shouldEqual Either.Right(MyType(TYPE_TEST))
    }

    @Test fun `should return correct data when executing use case`() {
        var result : Either<Failure, MyType>? = null
        val params = MyParams(TYPE_PARAM)
        val onResult = { myResult: Either<Failure, MyType> -> result = myResult }
        runBlocking { useCase(params, onResult).join() }
        result shouldEqual Either.Right(MyType(TYPE_TEST))
    }


    data class MyType(val name: String)
    data class MyParams(val name: String)

    private inner class MyUseCase : UseCase<MyType, MyParams>() {
        override suspend fun run(params: MyParams): Either.Right<MyType> {
            return Either.Right(MyType(TYPE_TEST))
        }
    }
}
