package core.db

import core.db.models.ModelAudio
import core.db.models.ModelFolder
import utils.Global
import java.sql.Connection
import java.sql.DriverManager

object CoreDB {

    private lateinit var connection: Connection

    //config
    fun init () {
        val dbFile = "${Global.dbPath}/plaudio.db"
        connection = DriverManager.getConnection("jdbc:sqlite:$dbFile")
        createTables()
    }
    fun resetDB () {
        dropTables()
        createTables()
    }

    private fun dropTables () {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS songs")
            statement.executeUpdate("DROP TABLE IF EXISTS folders")
        } catch (_: Exception) {} finally {
            statement.close()
        }
    }
    private fun createTables () {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS audios(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT, " +
                    "path TEXT, " +
                    "folder TEXT, " +
                    "artist TEXT, " +
                    "size INTEGER, " +
                    "duration INTEGER, " +
                    "coverArt TEXT, " +
                    "isFav INTEGER, " +
                    "format TEXT" +
                    ")")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS folders(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT, " +
                    "path TEXT, " +
                    "childCount INTEGER" +
                    ")")
        } catch (_ : Exception) {} finally {
            statement.close()
        }
    }

    object Audios {

        fun insert (ma: ModelAudio) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("insert into audios " +
                        "(name,path,folder,artist,size,duration,coverArt,isFav,format) " +
                        "values " +
                        "('${ma.name}','${ma.path}','${ma.folder}','${ma.artist}',${ma.size},${ma.duration},'${ma.coverArt}',${ma.isFav}," +
                        "'${ma.format}')")
            } catch (_: Exception) {} finally {
                statement.close()
            }
        }

        fun update (modelAudio: ModelAudio) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("update audios set " +
                        "name='${modelAudio.name}'," +
                        "artist='${modelAudio.artist}'," +
                        "size='${modelAudio.size}'," +
                        "coverArt='${modelAudio.coverArt}'" +
                        " where id='${modelAudio.id}'")
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun addToFav (id : Int) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("update audios set isFav = true where id = '$id'")
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun removeFromFav (id : Int) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("update audios set isFav = false where id = '$id'")
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun count () : Int {
            val statement = connection.createStatement()
            val query = statement.executeQuery("SELECT COUNT(*) AS countAudios FROM audios")
            val count = query.getInt("countAudios")
            statement.close()
            return count
        }

        fun countFavorites () : Int {
            val statement = connection.createStatement()
            val query = statement.executeQuery("SELECT COUNT(*) AS countAudios FROM audios WHERE isFav = true")
            val count = query.getInt("countAudios")
            statement.close()
            return count
        }

        fun read () : ArrayList<ModelAudio> {
            val statement = connection.createStatement()
            val query = statement.executeQuery("select * from audios order by name asc")
            val arrayOut = ArrayList<ModelAudio>()
            try {
                while (query.next()) {
                    val modelAudioItem = ModelAudio()
                    modelAudioItem.id = query.getInt("id")
                    modelAudioItem.name = query.getString("name")
                    modelAudioItem.path = query.getString("path")
                    modelAudioItem.folder = query.getString("folder")
                    modelAudioItem.artist = query.getString("artist")
                    modelAudioItem.size = query.getLong("size")
                    modelAudioItem.duration = query.getLong("duration")
                    modelAudioItem.coverArt = query.getString("coverArt")
                    modelAudioItem.isFav = query.getBoolean("isFav")
                    modelAudioItem.format = query.getString("format")
                    arrayOut.add(modelAudioItem)
                }
            } catch (_: Exception) {} finally {
                statement.close()
            }
            return arrayOut
        }

    }

    object Folders {

        fun insert (modelFolderItem: ModelFolder) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("insert into folders " +
                        "(name,path,childCount) " +
                        "values " +
                        "(\"${modelFolderItem.name}\",\"${modelFolderItem.path}\",${modelFolderItem.childCunt})")
            } catch (_: Exception) {} finally {
                statement.close()
            }
        }

        fun read () : ArrayList<ModelFolder> {
            val statement = connection.createStatement()
            val query = statement.executeQuery("select * from folders order by name asc")
            val arrayOut = ArrayList<ModelFolder>()
            try {
                while (query.next()) {
                    val modelFolderItem = ModelFolder()
                    modelFolderItem.id = query.getInt("id")
                    modelFolderItem.name = query.getString("name")
                    modelFolderItem.path = query.getString("path")
                    modelFolderItem.childCunt = query.getInt("childCount")
                    arrayOut.add(modelFolderItem)
                }
            } catch (_: Exception) {} finally {
                statement.close()
            }
            arrayOut.add(0,ModelFolder(name = "All Audios", path = "#All", childCunt = arrayOut.sumOf { it.childCunt }))
            arrayOut.add(1, ModelFolder(name = "Favorites", path = "#Fav", childCunt = Audios.countFavorites()))
            return arrayOut
        }

    }

}