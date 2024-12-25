package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.MediaCard
import com.muedsa.tvbox.api.data.MediaCardRow
import com.muedsa.tvbox.api.service.IMainScreenService
import com.muedsa.tvbox.gpd.DownloaderConsts
import com.muedsa.tvbox.gpd.GithubHelper

class MainScreenService : IMainScreenService {

    override suspend fun getRowsData(): List<MediaCardRow> {
        return listOf(
            MediaCardRow(
                title = "从Github下载TvBox插件",
                list = listOf(
                    MediaCard(
                        id = ActionDelegate.ACTION_INVALID,
                        title = "在搜索中用仓库全名(owner/repo)进行搜索",
                        subTitle = "说明",
                        detailUrl = "",
                        coverImageUrl = "https://github.githubassets.com/assets/github-mark-57519b92ca4e.png",
                    ),
                    MediaCard(
                        id = ActionDelegate.ACTION_INVALID,
                        title = "收藏可以方便下次使用",
                        subTitle = "注意",
                        detailUrl = "",
                        coverImageUrl = "https://github.githubassets.com/assets/github-octocat-13c86b8b336d.png",
                    ),
                    MediaCard(
                        id = "muedsa/github-plugin-downloader",
                        title = "muedsa/github-plugin-downloader",
                        subTitle = "本插件仓库",
                        detailUrl = "muedsa/github-plugin-downloader",
                        coverImageUrl = GithubHelper.createRepoGraphImageUrl("muedsa/github-plugin-downloader"),
                    ),
                ),
                cardWidth = DownloaderConsts.REPO_CARD_WIDTH,
                cardHeight = DownloaderConsts.REPO_CARD_HEIGHT,
            )
        )
    }
}