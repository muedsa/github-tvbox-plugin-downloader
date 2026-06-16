package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.gpd.checkMediaCardRows
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MainScreenServiceTest {

    private val service = MainScreenService()

    @Test
    fun getRowsDataTest() = runTest{
        val rows = service.getRowsData()
        checkMediaCardRows(rows = rows)
    }

}