package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.MediaDetail
import com.muedsa.tvbox.api.data.MediaHttpSource
import com.muedsa.tvbox.gpd.DownloaderPlugin
import com.muedsa.tvbox.gpd.model.Asset
import com.muedsa.tvbox.tool.LenientJson
import com.muedsa.tvbox.tool.toRequestBuild
import okhttp3.OkHttpClient
import java.io.FileOutputStream
import java.io.IOException

class ActionDelegate(
    private val okHttpClient: OkHttpClient,
) {

    fun execAsGetDetailData(action: String, data: String): MediaDetail {
        val mediaDetail = when (action) {
            else -> throw IllegalArgumentException("未知动作 $action")
        }
        return mediaDetail
    }

    fun execAsGetEpisodePlayInfo(action: String, data: String?): MediaHttpSource {
        val message = when (action) {

            ACTION_DOWNLOAD_ASSET -> downloadAsset(
                LenientJson.decodeFromString(data ?: throw IllegalArgumentException("文件URL?"))
            )

            else -> "未知动作 $action"
        }
        throw IllegalArgumentException(message)
    }

    // TODO 使用 DownloadManager, 但是目前API未暴露 TvBox 的 App Context
    fun downloadAsset(asset: Asset): String {
        val file = DownloaderPlugin.DOWNLOAD_DIR.resolve(asset.name)
        if (file.isDirectory) {
            throw IllegalStateException("存在重名的目录,请手动删除 ${file.absolutePath}")
        }
        if(!file.canWrite()) {
            throw IllegalStateException("TvBox没有文件管理权限,请手动授权")
        }
        val request = asset.browserDownloadUrl.toRequestBuild().build()
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            val file = DownloaderPlugin.DOWNLOAD_DIR.resolve(asset.name)
            response.body?.byteStream()?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } != -1) {
                        outputStream.write(buffer, 0, length)
                    }
                }
            }
            return "已下载到 ${file.absolutePath}"
        }
    }

    companion object {
        const val ACTION_PREFIX = "action_"
        const val ACTION_INVALID = "${ACTION_PREFIX}invalid"
        const val ACTION_DOWNLOAD_ASSET = "${ACTION_PREFIX}download_asset"
    }
}