package com.tablereservation.core.functional

import com.tablereservation.UnitTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test

class EitherTest : UnitTest() {

    private val arbitraryValue = "arbitrary value"

    @Test fun `Either Right should return correct type`() {
        val result = Either.Right(arbitraryValue)

        result shouldBeInstanceOf Either::class.java
        result.isRight shouldBe true
        result.isLeft shouldBe false
        result.either({},
                { right ->
                    right shouldBeInstanceOf String::class.java
                    right shouldBeEqualTo arbitraryValue
                })
    }

    @Test fun `Either Left should return correct type`() {
        val result = Either.Left(arbitraryValue)

        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldBe true
        result.isRight shouldBe false
        result.either(
                { left ->
                    left shouldBeInstanceOf String::class.java
                    left shouldBeEqualTo arbitraryValue
                }, {})
    }
}