package com.muedsa.tvbox.gpd

import com.muedsa.tvbox.api.plugin.TvBoxContext
import com.muedsa.tvbox.tool.IPv6Checker
import com.muedsa.tvbox.tool.createOkHttpClient
import okhttp3.CookieJar

val TestPlugin by lazy {
    DownloaderPlugin(
        tvBoxContext = TvBoxContext(
            screenWidth = 1920,
            screenHeight = 1080,
            debug = true,
            store = FakePluginPrefStore(),
            iPv6Status = IPv6Checker.checkIPv6Support()
        )
    )
}

val TestOkHttpClient by lazy {
    createOkHttpClient(
        debug = true,
        cookieJar = CookieJar.NO_COOKIES,
        onlyIpv4 = true
    )
}