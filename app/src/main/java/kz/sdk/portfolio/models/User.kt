package kz.sdk.portfolio.models

data class User(
    var name: String? = null,
    var lastname: String?= null,
    var phone:String?= null,
    var pictureUrl: String? = null,
    var isAdmin:Boolean = false,
)