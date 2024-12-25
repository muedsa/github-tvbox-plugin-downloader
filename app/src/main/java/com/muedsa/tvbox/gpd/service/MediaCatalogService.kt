package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.MediaCard
import com.muedsa.tvbox.api.data.MediaCatalogConfig
import com.muedsa.tvbox.api.data.MediaCatalogOption
import com.muedsa.tvbox.api.data.PagingResult
import com.muedsa.tvbox.api.service.IMediaCatalogService
import com.muedsa.tvbox.gpd.DownloaderConsts
import com.muedsa.tvbox.gpd.GithubHelper
import com.muedsa.tvbox.tool.checkSuccess
import com.muedsa.tvbox.tool.feignChrome
import com.muedsa.tvbox.tool.get
import com.muedsa.tvbox.tool.parseHtml
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class MediaCatalogService(
    private val okHttpClient: OkHttpClient,
) : IMediaCatalogService {

    override suspend fun getConfig(): MediaCatalogConfig {

        return MediaCatalogConfig(
            initKey = "1",
            pageSize = 20,
            cardWidth = DownloaderConsts.REPO_CARD_WIDTH,
            cardHeight = DownloaderConsts.REPO_CARD_HEIGHT,
            catalogOptions = emptyList(),
        )
    }

    override suspend fun catalog(
        options: List<MediaCatalogOption>,
        loadKey: String,
        loadSize: Int
    ): PagingResult<MediaCard> {
        val page = loadKey.toInt()
        val body = Request.Builder()
            .url(
                "https://github.com/topics/tvbox-plugin".toHttpUrl()
                    .newBuilder()
                    .addQueryParameter("page", loadKey)
                    .build()
            )
            .feignChrome()
            .get(okHttpClient = okHttpClient)
            .checkSuccess()
            .parseHtml()
            .body()
        val articleEls = body.select(".application-main main .topic article")
        return PagingResult<MediaCard>(
            list = articleEls.map { articleEl ->
                val repoFullName =
                    articleEl.selectFirst(">div >div >div >h3")!!.text().replace(" ", "")
                val desc = articleEl.selectFirst(">div >div >p")!!.text().trim()
                val customImageUrl = articleEl.child(0).let {
                    if (it.tagName() == "a") {
                        it.selectFirst("img")?.attr("src")
                    } else null
                }
                MediaCard(
                    id = repoFullName,
                    title = repoFullName,
                    detailUrl = repoFullName,
                    subTitle = desc,
                    coverImageUrl = customImageUrl ?: GithubHelper.createRepoGraphImageUrl(
                        repoFullName
                    )
                )
            },
            nextKey = if (body.selectFirst(".ajax-pagination-form") != null) "${page + 1}" else null,
            prevKey = if (page > 1) "${page - 1}" else null
        )
    }
}