package com.muedsa.tvbox.gpd

import com.muedsa.tvbox.tool.stringBody
import okhttp3.Response

object DownloaderConsts {
    const val REPO_CARD_WIDTH = 200
    const val REPO_CARD_HEIGHT = 100

    val GITHUB_API_RESP_CHECKER = { resp: Response ->
        if (!resp.isSuccessful) {
            throw RuntimeException("请求失败 ${resp.stringBody()}")
        }
    }
}