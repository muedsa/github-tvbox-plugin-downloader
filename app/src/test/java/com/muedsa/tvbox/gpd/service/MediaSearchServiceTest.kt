package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.gpd.TestGithubApiService
import com.muedsa.tvbox.gpd.checkMediaCardRow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MediaSearchServiceTest {

    private val service = MediaSearchService(
        githubApiService = TestGithubApiService,
    )

    @Test
    fun searchMedias_test() = runTest {
        val row = service.searchMedias("muedsa/github-plugin-downloader")
        checkMediaCardRow(row = row)
    }
}