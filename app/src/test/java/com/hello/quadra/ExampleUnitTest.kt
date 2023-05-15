package com.hello.quadra

import org.junit.Test

import org.junit.Assert.*
import java.lang.Integer.sum

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sum_addsTwoNumbers() {
        val result = sum(1, 2)
        assertEquals(3, result)
    }
}