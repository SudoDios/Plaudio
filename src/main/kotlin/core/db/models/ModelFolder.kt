package core.db.models

data class ModelFolder(
    var id : Int = -1,
    var name : String = "All Audios",
    var type : Int = TYPE_FOLDER,
    var path : String = "#All",
    var duration : Long = 0,
    var childCunt : Int = 0,
) {

    companion object {
        const val TYPE_FOLDER = 0
        const val TYPE_ALBUM = 1
        const val TYPE_ARTIST = 2
        const val TYPE_PLAYLIST = 3
    }

}
