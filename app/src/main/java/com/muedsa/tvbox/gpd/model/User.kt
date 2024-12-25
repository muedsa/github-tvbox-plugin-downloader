package com.muedsa.tvbox.gpd.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val login: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatarUrl: String,
    // more
    val type: String,
    @SerialName("user_view_type") val userViewType: String,
    @SerialName("site_admin") val siteAdmin: Boolean,
)
