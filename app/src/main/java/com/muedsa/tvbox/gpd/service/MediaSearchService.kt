package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.MediaCard
import com.muedsa.tvbox.api.data.MediaCardRow
import com.muedsa.tvbox.api.service.IMediaSearchService
import com.muedsa.tvbox.gpd.DownloaderConsts
import com.muedsa.tvbox.gpd.GithubHelper
import com.muedsa.tvbox.gpd.model.Repository
import com.muedsa.tvbox.tool.LenientJson
import com.muedsa.tvbox.tool.checkSuccess
import com.muedsa.tvbox.tool.get
import com.muedsa.tvbox.tool.stringBody
import com.muedsa.tvbox.tool.toRequestBuild
import okhttp3.OkHttpClient

class MediaSearchService(
    private val okHttpClient: OkHttpClient,
) : IMediaSearchService {

    override suspend fun searchMedias(query: String): MediaCardRow {
        val ownerAndRepo = query.split("/")
        check(ownerAndRepo.size == 2) { "请按照 owner/repo 格式输入进行搜索" }
        var resp = "https://api.github.com/repos/${ownerAndRepo[0]}/${ownerAndRepo[1]}"
            .toRequestBuild()
            .get(okHttpClient = okHttpClient)
            .checkSuccess(DownloaderConsts.GITHUB_API_RESP_CHECKER)
        val repo = LenientJson.decodeFromString<Repository>(resp.stringBody())
        return MediaCardRow(
            title = "repo",
            list = listOf(
                MediaCard(
                    id = repo.fullName,
                    title = repo.fullName,
                    detailUrl = repo.fullName,
                    coverImageUrl = GithubHelper.createRepoGraphImageUrl(repo.fullName),
                    subTitle = repo.updatedAt
                )
            ),
            cardWidth = DownloaderConsts.REPO_CARD_WIDTH,
            cardHeight = DownloaderConsts.REPO_CARD_HEIGHT,
        )
    }
}