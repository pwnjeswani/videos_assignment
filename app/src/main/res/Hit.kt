
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Hit(
    @SerializedName("comments")
    val comments: Int?,
    @SerializedName("downloads")
    val downloads: Int?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("pageURL")
    val pageURL: String?,
    @SerializedName("picture_id")
    val pictureId: String?,
    @SerializedName("tags")
    val tags: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("user")
    val user: String?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("userImageURL")
    val userImageURL: String?,
    @SerializedName("videos")
    val videos: Videos?,
    @SerializedName("views")
    val views: Int?
) : Parcelable