package kz.sdk.portfolio.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    var id:String? = null,
    var title:String? = null,
    var img:String? = null,
    var description:String? = null,
    var videoUrl:String? = null,
): Parcelable