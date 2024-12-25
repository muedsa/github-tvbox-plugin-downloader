package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.gpd.TestOkHttpClient
import com.muedsa.tvbox.gpd.checkMediaCardRow
import kotlinx.coroutines.test.runTest
import org.junit.Test

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