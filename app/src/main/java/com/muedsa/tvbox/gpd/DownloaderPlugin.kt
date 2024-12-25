package com.muedsa.tvbox.gpd

import android.os.Environment
import com.muedsa.tvbox.api.plugin.IPlugin
import com.muedsa.tvbox.api.plugin.PluginOptions
import com.muedsa.tvbox.api.plugin.TvBoxContext
import com.muedsa.tvbox.api.service.IMainScreenService
import com.muedsa.tvbox.api.service.IMediaCatalogService
import com.muedsa.tvbox.api.service.IMediaDetailService
import com.muedsa.tvbox.api.service.IMediaSearchService
import com.muedsa.tvbox.gpd.service.GithubApiService
import com.muedsa.tvbox.gpd.service.MainScreenService
import com.muedsa.tvbox.gpd.service.MediaCatalogService
import com.muedsa.tvbox.gpd.service.MediaDetailService
import com.muedsa.tvbox.gpd.service.MediaSearchService
import com.muedsa.tvbox.tool.IPv6Checker
import com.muedsa.tvbox.tool.createJsonRetrofit
import com.muedsa.tvbox.tool.createOkHttpClient
import okhttp3.CookieJar
import java.io.File

class DownloaderPlugin(tvBoxContext: TvBoxContext) : IPlugin(tvBoxContext = tvBoxContext) {

    private val okHttpClient by lazy {
        createOkHttpClient(
            debug = tvBoxContext.debug,
            cookieJar = CookieJar.NO_COOKIES,
            onlyIpv4 = tvBoxContext.iPv6Status != IPv6Checker.IPv6Status.SUPPORTED
        )
    }
    private val githubApiService by lazy {
        createJsonRetrofit(
            baseUrl = "https://api.github.com/",
            service = GithubApiService::class.java,
            okHttpClient = okHttpClient
        )
    }

    private val mainScreenService by lazy { MainScreenService() }
    private val mediaDetailService by lazy {
        MediaDetailService(
            okHttpClient = okHttpClient,
            githubApiService = githubApiService,
        )
    }
    private val mediaSearchService by lazy {
        MediaSearchService(
            githubApiService = githubApiService,
        )
    }
    private val mediaCatalogService by lazy {
        MediaCatalogService(
            okHttpClient = okHttpClient,
        )
    }

    override fun provideMainScreenService(): IMainScreenService = mainScreenService
    override fun provideMediaDetailService(): IMediaDetailService = mediaDetailService
    override fun provideMediaSearchService(): IMediaSearchService = mediaSearchService
    override fun provideMediaCatalogService(): IMediaCatalogService = mediaCatalogService

    override var options: PluginOptions = PluginOptions(enableDanDanPlaySearch = false)
    override suspend fun onInit() {}
    override suspend fun onLaunched() {}

    companion object {
        val DOWNLOAD_DIR: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
}