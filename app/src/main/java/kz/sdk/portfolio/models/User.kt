package kz.sdk.portfolio.models

data class User(
    var name: String? = null,
    var lastname: String?= null,
    var phone:String?= null,
    var pictureUrl: String? = null,
    var isAdmin:Boolean = false,
    var educations: Map<String, Education> = emptyMap(),
    var licenses:Map<String, License> = emptyMap(),
    var skills:Map<String, Skill> = emptyMap(),
    var rewards:Map<String, Reward> = emptyMap(),

)