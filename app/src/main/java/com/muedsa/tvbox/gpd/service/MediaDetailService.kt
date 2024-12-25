package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.api.data.DanmakuData
import com.muedsa.tvbox.api.data.DanmakuDataFlow
import com.muedsa.tvbox.api.data.MediaDetail
import com.muedsa.tvbox.api.data.MediaEpisode
import com.muedsa.tvbox.api.data.MediaHttpSource
import com.muedsa.tvbox.api.data.MediaPlaySource
import com.muedsa.tvbox.api.data.SavedMediaCard
import com.muedsa.tvbox.api.service.IMediaDetailService
import com.muedsa.tvbox.gpd.DownloaderConsts
import com.muedsa.tvbox.gpd.GithubHelper
import com.muedsa.tvbox.tool.LenientJson
import kotlinx.serialization.encodeToString
import okhttp3.OkHttpClient

class MediaDetailService(
    private val okHttpClient: OkHttpClient,
    private val githubApiService: GithubApiService,
) : IMediaDetailService {

    private val actionDelegate by lazy {
        ActionDelegate(
            okHttpClient = okHttpClient,
        )
    }

    override suspend fun getDetailData(mediaId: String, detailUrl: String): MediaDetail {
        if (mediaId.startsWith(ActionDelegate.ACTION_PREFIX)) {
            return actionDelegate.execAsGetDetailData(mediaId, detailUrl)
        }
        val (owner, repo) = mediaId.split("/")
        val repository = githubApiService.repo(owner = owner, repo = repo)
        val release = githubApiService.latestRelease(owner = owner, repo = repo)
        val repoImageUrl = GithubHelper.createRepoGraphImageUrl(repository.fullName)
        return MediaDetail(
            id = repository.fullName,
            title = repository.fullName,
            subTitle = repository.owner.login,
            description = repository.description,
            detailUrl = repository.fullName,
            backgroundImageUrl = repoImageUrl,
            playSourceList = listOf(
                MediaPlaySource(
                    id = "github",
                    name = "github",
                    episodeList = buildList {
                        release.assets
                            .filter { it.name.endsWith(".tbp") && it.contentType == "application/octet-stream" }
                            .forEach {
                                add(
                                    MediaEpisode(
                                        id = ActionDelegate.ACTION_DOWNLOAD_ASSET,
                                        name = "下载 ${it.name}",
                                        flag5 = LenientJson.encodeToString(it),
                                    )
                                )
                                add(
                                    MediaEpisode(
                                        id = ActionDelegate.ACTION_DOWNLOAD_ASSET,
                                        name = "ghp下载 ${it.name}",
                                        flag5 = LenientJson.encodeToString(
                                            it.copy(
                                                browserDownloadUrl = "https://ghgo.xyz/${it.browserDownloadUrl}",
                                            )
                                        ),
                                    )
                                )
                                add(
                                    MediaEpisode(
                                        id = ActionDelegate.ACTION_DOWNLOAD_ASSET,
                                        name = "gh-proxy下载 ${it.name}",
                                        flag5 = LenientJson.encodeToString(
                                            it.copy(
                                                browserDownloadUrl = it.browserDownloadUrl
                                                    .replaceFirst(
                                                        "https://github.com/",
                                                        "https://gh-proxy.com/github.com/"
                                                    )
                                            )
                                        ),
                                    )
                                )
                            }
                    }
                )
            ),
            favoritedMediaCard = SavedMediaCard(
                id = repository.fullName,
                title = repository.fullName,
                detailUrl = repository.fullName,
                coverImageUrl = repoImageUrl,
                cardWidth = DownloaderConsts.REPO_CARD_WIDTH,
                cardHeight = DownloaderConsts.REPO_CARD_HEIGHT,
            ),
            disableEpisodeProgression = true
        )
    }

    override suspend fun getEpisodePlayInfo(
        playSource: MediaPlaySource,
        episode: MediaEpisode
    ): MediaHttpSource {
        if (episode.id.startsWith(ActionDelegate.ACTION_PREFIX)) {
            return actionDelegate.execAsGetEpisodePlayInfo(episode.id, episode.flag5)
        }
        throw IllegalArgumentException("?")
    }

    override suspend fun getEpisodeDanmakuDataList(episode: MediaEpisode): List<DanmakuData>
        = emptyList()

    override suspend fun getEpisodeDanmakuDataFlow(episode: MediaEpisode): DanmakuDataFlow? = null
}