package core.db.models

data class ModelAudio(
    var id : Int = -1,
    var name : String = "",
    var duration : Long = -1,
    var size : Long = -1,
    var folder : String = "",
    var artist : String = "Unknown",
    var album : String = "Unknown",
    var path : String = "",
    var coverArt : String? = null,
    var isFav : Boolean = false,
    var hash : String = "",
    var playCount : Int = 0,
    var format : String = ""
)
