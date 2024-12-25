package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.MediaCard
import com.muedsa.tvbox.api.data.MediaCardRow
import com.muedsa.tvbox.api.service.IMediaSearchService
import com.muedsa.tvbox.gpd.DownloaderConsts
import com.muedsa.tvbox.gpd.GithubHelper

class MediaSearchService(
    private val githubApiService: GithubApiService,
) : IMediaSearchService {

    override suspend fun searchMedias(query: String): MediaCardRow {
        val ownerAndRepo = query.split("/")
        check(ownerAndRepo.size == 2) { "请按照 owner/repo 格式输入进行搜索" }
        val repo = githubApiService.repo(ownerAndRepo[0], ownerAndRepo[1])
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