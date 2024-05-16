package com.newolf.notificationdemo

import org.junit.Test

import org.junit.Assert.*
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    companion object {
        const val TAG = "wolf_test"
        const val ONE_DAY = 24 * 60 * 60 * 1000 - 10
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun isSameDay() {

        val current = System.currentTimeMillis()
        val last = System.currentTimeMillis() - 16 * 60 * 60 * 1000


        if (current - last > ONE_DAY) {
            println( "Is same day")
        }
        val diff = Date(current).getDate() - Date(last).getDate()
        assertEquals(0, diff)

    }

    @Test
    fun isDiffDay(): Unit {

        val current = System.currentTimeMillis()
        val last = System.currentTimeMillis() - (17 * 60 * 60 * 1000 + ONE_DAY)



        if (current - last > ONE_DAY) {
            println( "Is same day")
        }
        val diff = Date(current).getDate() - Date(last).getDate()
        println("diff = $diff , current = ${Date(current).getDate()} , last = ${Date(last).getDate()}" )
        assertEquals(2, diff)

    }
}