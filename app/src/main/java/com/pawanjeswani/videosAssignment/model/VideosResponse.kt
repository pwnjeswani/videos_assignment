package com.pawanjeswani.videosAssignment.model


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideosResponse(
    @SerializedName("hits")
    val hits: List<Hit?>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("totalHits")
    val totalHits: Int?
) : Parcelable

@Parcelize
data class Hit(
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("pageURL")
    val pageURL: String?,
    @SerializedName("picture_id")
    val pictureId: String?,
    @SerializedName("user")
    val user: String?,
    @SerializedName("userImageURL")
    val userImageURL: String?,
    @SerializedName("videos")
    val videos: Videos?
) : Parcelable

@Parcelize
data class Videos(
    @SerializedName("medium")
    val medium: VideoInfo?,
) : Parcelable

@Parcelize
data class VideoInfo(
    @SerializedName("url")
    val url: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("pageURL")
    val pageURL: String?,
) : Parcelable