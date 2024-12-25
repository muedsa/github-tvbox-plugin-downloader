package com.muedsa.tvbox.gpd.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    @SerialName("url") val url: String,
    @SerialName("assets_url") val assetsUrl: String,
    @SerialName("upload_url") val uploadUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("id") val id: Int,
    @SerialName("author") val author: User,
    @SerialName("node_id") val nodeId: String,
    @SerialName("tag_name") val tagName: String,
    @SerialName("target_commitish") val targetCommitish: String,
    @SerialName("name") val name: String,
    @SerialName("draft") val draft: Boolean,
    @SerialName("prerelease") val preRelease: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("assets") val assets: List<Asset>,
    @SerialName("tarball_url") val tarBallUrl: String,
    @SerialName("zipball_url") val zipBallUrl: String,
    @SerialName("body") val body: String,
)
