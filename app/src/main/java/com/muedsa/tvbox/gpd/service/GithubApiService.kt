package com.muedsa.tvbox.gpd.service

import com.muedsa.tvbox.gpd.model.Release
import com.muedsa.tvbox.gpd.model.Repository
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {

    @GET("repos/{owner}/{repo}")
    suspend fun repo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): Repository

    @GET("repos/{owner}/{repo}/releases/latest")
    suspend fun latestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): Release
}