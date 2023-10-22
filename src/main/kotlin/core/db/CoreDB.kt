package core.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import core.db.models.ModelAudio
import core.db.models.ModelPlaylist
import utils.Global
import java.sql.Connection
import java.sql.DriverManager

object CoreDB {

    private lateinit var connection: Connection

    //config
    fun init () {
        val dbFile = "${Global.dbPath}/plaudio.db"
        Class.forName("org.sqlite.JDBC");
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
            statement.executeUpdate("DROP TABLE IF EXISTS audios")
            statement.executeUpdate("DROP TABLE IF EXISTS waveforms")
            statement.executeUpdate("DROP TABLE IF EXISTS playlists")
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
                    "album TEXT, " +
                    "size INTEGER, " +
                    "duration INTEGER, " +
                    "coverArt TEXT, " +
                    "isFav INTEGER, " +
                    "hash TEXT, " +
                    "playCount INTEGER, " +
                    "format TEXT" +
                    ")")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS playlists(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "childes TEXT" +
                    ")")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS waveforms(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "hash TEXT," +
                    "wave TEXT" +
                    ")")
        } catch (_ : Exception) {} finally {
            statement.close()
        }
    }

    object Waveforms {

        fun saveWaveform (hash: String,waveform : FloatArray) {
            val statement = connection.prepareStatement("INSERT INTO waveforms (hash,wave) VALUES (?,?)")
            try {
                statement.setString(1,hash)
                statement.setString(2,Gson().toJson(waveform))
                statement.executeUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                statement.close()
            }
        }

        fun getWaveform (hash: String) : FloatArray? {
            val statement = connection.prepareStatement("SELECT wave FROM waveforms where hash=?")
            statement.setString(1,hash)
            val query = statement.executeQuery()
            val wave = query.getString("wave")
            statement.close()
            return if (wave != null) {
                Gson().fromJson(wave, FloatArray::class.java)
            } else {
                null
            }
        }

    }

    object Audios {

        fun insert (ma: ModelAudio) {
            val query = "insert into audios " +
                    "(name,path,folder,artist,album,size,duration,coverArt,isFav,hash,playCount,format) " +
                    "values " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?)"
            val preparedStatement = connection.prepareStatement(query)
            try {
                preparedStatement.setString(1,ma.name)
                preparedStatement.setString(2,ma.path)
                preparedStatement.setString(3,ma.folder)
                preparedStatement.setString(4,ma.artist)
                preparedStatement.setString(5,ma.album)
                preparedStatement.setLong(6,ma.size)
                preparedStatement.setLong(7,ma.duration)
                preparedStatement.setString(8,ma.coverArt)
                preparedStatement.setBoolean(9,ma.isFav)
                preparedStatement.setString(10,ma.hash)
                preparedStatement.setInt(11,ma.playCount)
                preparedStatement.setString(12,ma.format)
                preparedStatement.executeUpdate()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                preparedStatement.close()
            }
        }

        fun updatePlayCount (hash : String) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("update audios set " +
                        "playCount = playCount + 1" +
                        " where hash='$hash'")
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun update (modelAudio: ModelAudio) {
            val query = "update audios set " +
                    "name=?," +
                    "artist=?," +
                    "album=?," +
                    "size=?," +
                    "coverArt=?," +
                    "hash=? " +
                    "where id=?"
            val statement = connection.prepareStatement(query)
            try {
                statement.setString(1,modelAudio.name)
                statement.setString(2,modelAudio.artist)
                statement.setString(3,modelAudio.album)
                statement.setLong(4,modelAudio.size)
                statement.setString(5,modelAudio.coverArt)
                statement.setString(6,modelAudio.hash)
                statement.setInt(7,modelAudio.id)
                statement.executeUpdate()
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun addToFav (hash: String) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("update audios set isFav = true where hash = '$hash'")
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun removeFromFav (hash: String) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("update audios set isFav = false where hash = '$hash'")
            } catch (_ : Exception) {} finally {
                statement.close()
            }
        }

        fun clearAll () {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("DELETE FROM audios")
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

        fun readMostPlayed () : ArrayList<ModelAudio> {
            val statement = connection.createStatement()
            val query = statement.executeQuery("select * from audios WHERE playCount > 0 order by playCount desc limit 10")
            val arrayOut = ArrayList<ModelAudio>()
            try {
                while (query.next()) {
                    val modelAudioItem = ModelAudio()
                    modelAudioItem.id = query.getInt("id")
                    modelAudioItem.name = query.getString("name")
                    modelAudioItem.path = query.getString("path")
                    modelAudioItem.folder = query.getString("folder")
                    modelAudioItem.artist = query.getString("artist")
                    modelAudioItem.album = query.getString("album")
                    modelAudioItem.size = query.getLong("size")
                    modelAudioItem.duration = query.getLong("duration")
                    modelAudioItem.coverArt = query.getString("coverArt")
                    modelAudioItem.isFav = query.getBoolean("isFav")
                    modelAudioItem.hash = query.getString("hash")
                    modelAudioItem.playCount = query.getInt("playCount")
                    modelAudioItem.format = query.getString("format")
                    arrayOut.add(modelAudioItem)
                }
            } catch (_: Exception) {} finally {
                statement.close()
            }
            return arrayOut
        }

        fun read (sortIn : Int = Global.Data.SORT_NAME_ASC) : ArrayList<ModelAudio> {
            val statement = connection.createStatement()
            val sort = when(sortIn) {
                Global.Data.SORT_NAME_ASC -> "name asc"
                Global.Data.SORT_NAME_DESC -> "name desc"
                Global.Data.SORT_DURATION_ASC -> "duration asc"
                else -> "duration desc"
            }
            val query = statement.executeQuery("select * from audios order by $sort")
            val arrayOut = ArrayList<ModelAudio>()
            try {
                while (query.next()) {
                    val modelAudioItem = ModelAudio()
                    modelAudioItem.id = query.getInt("id")
                    modelAudioItem.name = query.getString("name")
                    modelAudioItem.path = query.getString("path")
                    modelAudioItem.folder = query.getString("folder")
                    modelAudioItem.artist = query.getString("artist")
                    modelAudioItem.album = query.getString("album")
                    modelAudioItem.size = query.getLong("size")
                    modelAudioItem.duration = query.getLong("duration")
                    modelAudioItem.coverArt = query.getString("coverArt")
                    modelAudioItem.isFav = query.getBoolean("isFav")
                    modelAudioItem.hash = query.getString("hash")
                    modelAudioItem.playCount = query.getInt("playCount")
                    modelAudioItem.format = query.getString("format")
                    arrayOut.add(modelAudioItem)
                }
            } catch (_: Exception) {} finally {
                statement.close()
            }
            return arrayOut
        }

    }

    object Playlists {

        fun insert (mp: ModelPlaylist,inserted : (Int) -> Unit) {
            val statement = connection.createStatement()
            try {
                statement.executeUpdate("insert into playlists (name,childes) values ('${mp.name}','[]')")
                val insertedId = statement.executeQuery("select last_insert_rowid() as insertedId").getInt("insertedId")
                inserted.invoke(insertedId)
            } catch (e: Exception) {
                e.printStackTrace()
                inserted.invoke(-1)
            } finally {
                statement.close()
                inserted.invoke(-1)
            }
        }

        fun addAudioItem (audioHash : String,vararg playlistIds : Int) {
            val statement = connection.createStatement()
            try {
                playlistIds.forEach {
                    // -> find playlist
                    val queryFind = statement.executeQuery("select childes from playlists where id = '$it'")
                    if (queryFind.next()) {
                        val playlistChildes = Gson().fromJson<ArrayList<String>>(queryFind.getString("childes"), TypeToken.getParameterized(ArrayList::class.java,String::class.java).type)
                        if (!playlistChildes.contains(audioHash)) {
                            playlistChildes.add(audioHash)
                        }
                        // -> update playlist
                        statement.executeUpdate("update playlists set childes = '${Gson().toJson(playlistChildes)}' where id = '$it'")
                    }
                }
            } catch (e : Exception) {
                e.printStackTrace()
            } finally {
                statement.close()
            }
        }

        fun removeAudioItem (audioHash : String,vararg playlistIds : Int) {
            val statement = connection.createStatement()
            try {
                playlistIds.forEach {
                    // -> find playlist
                    val queryFind = statement.executeQuery("select childes from playlists where id = '$it'")
                    if (queryFind.next()) {
                        val playlistChildes = Gson().fromJson<ArrayList<String>>(queryFind.getString("childes"), TypeToken.getParameterized(ArrayList::class.java,String::class.java).type)
                        playlistChildes.remove(audioHash)
                        // -> update playlist
                        statement.executeUpdate("update playlists set childes = '${Gson().toJson(playlistChildes)}' where id = '$it'")
                    }
                }
            } catch (e : Exception) {
                e.printStackTrace()
            } finally {
                statement.close()
            }
        }

        fun read () : ArrayList<ModelPlaylist> {
            val statement = connection.createStatement()
            val query = statement.executeQuery("select * from playlists order by name asc")
            val arrayOut = ArrayList<ModelPlaylist>()
            try {
                while (query.next()) {
                    val modelPlaylist = ModelPlaylist()
                    modelPlaylist.id= query.getInt("id")
                    modelPlaylist.name = query.getString("name")
                    modelPlaylist.childes = Gson().fromJson(query.getString("childes"), TypeToken.getParameterized(ArrayList::class.java,String::class.java).type)
                    arrayOut.add(modelPlaylist)
                }
            } catch (_: Exception) {} finally {
                statement.close()
            }
            return arrayOut
        }

    }

}