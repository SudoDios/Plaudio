package core.db.models

data class ModelPlaylist(
    var id : Int? = null,
    var name : String = "",
    var childes : ArrayList<String> = arrayListOf()
)
