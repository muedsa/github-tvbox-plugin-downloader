package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.gpd.TestOkHttpClient
import com.muedsa.tvbox.gpd.checkMediaCardRow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MediaSearchServiceTest {

    private val service = MediaSearchService(
        okHttpClient = TestOkHttpClient,
    )

    @Test
    fun searchMedias_test() = runTest {
        val row = service.searchMedias("muedsa/github-plugin-downloader")
        checkMediaCardRow(row = row)
    }
}