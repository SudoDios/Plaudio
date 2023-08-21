package core.db.models

data class ModelFolder(
    var id : Int? = null,
    var name : String = "All Audios",
    var path : String = "#All",
    var childCunt : Int = 0
)
