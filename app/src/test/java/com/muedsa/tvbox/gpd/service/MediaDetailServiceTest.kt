package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.MediaCardType
import com.muedsa.tvbox.gpd.TestGithubApiService
import com.muedsa.tvbox.gpd.TestOkHttpClient
import com.muedsa.tvbox.gpd.checkMediaCard
import com.muedsa.tvbox.gpd.checkMediaCardRow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MediaDetailServiceTest {

    private val service = MediaDetailService(
        okHttpClient = TestOkHttpClient,
        githubApiService = TestGithubApiService,
    )

    @Test
    fun getDetailData_test() = runTest{
        val detail = service.getDetailData("muedsa/github-plugin-downloader", "muedsa/github-plugin-downloader")
        check(detail.id.isNotEmpty())
        check(detail.title.isNotEmpty())
        check(detail.detailUrl.isNotEmpty())
        check(detail.backgroundImageUrl.isNotEmpty())
        detail.favoritedMediaCard?.let { favoritedMediaCard ->
            checkMediaCard(favoritedMediaCard, cardType = MediaCardType.STANDARD)
            check(favoritedMediaCard.cardWidth > 0)
            check(favoritedMediaCard.cardHeight > 0)
        }
        check(detail.playSourceList.isNotEmpty())
        detail.playSourceList.forEach { mediaPlaySource ->
            check(mediaPlaySource.id.isNotEmpty())
            check(mediaPlaySource.name.isNotEmpty())
            check(mediaPlaySource.episodeList.isNotEmpty())
            mediaPlaySource.episodeList.forEach {
                check(it.id.isNotEmpty())
                check(it.name.isNotEmpty())
            }
        }
        detail.rows.forEach {
            checkMediaCardRow(it)
        }
    }

    @Test
    fun getEpisodePlayInfo_test() = runTest{
        val detail = service.getDetailData("muedsa/github-plugin-downloader", "muedsa/github-plugin-downloader")
        check(detail.playSourceList.isNotEmpty())
        check(detail.playSourceList.flatMap { it.episodeList }.isNotEmpty())
        val mediaPlaySource = detail.playSourceList[0]
        val mediaEpisode = mediaPlaySource.episodeList[0]
        val playInfo = service.getEpisodePlayInfo(mediaPlaySource, mediaEpisode)
        check(playInfo.url.isNotEmpty())
    }

}