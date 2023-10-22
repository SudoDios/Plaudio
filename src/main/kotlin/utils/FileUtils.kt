package utils

import java.io.*
import java.security.MessageDigest
import java.util.zip.ZipFile


object FileUtils {

    fun File.md5() : String {
        val md = MessageDigest.getInstance("MD5")
        val result = this.inputStream().use { fis ->
            val buffer = ByteArray(8192)
            generateSequence {
                when (val bytesRead = fis.read(buffer)) {
                    -1 -> null
                    else -> bytesRead
                }
            }.forEach { bytesRead -> md.update(buffer, 0, bytesRead) }
            md.digest().joinToString("") { "%02x".format(it) }
        }
        return result
    }

    fun writeThumbImage(array: ByteArray,mimeType : String) : String? {
        val mime = if (mimeType.contains("/")) mimeType.substringAfterLast("/") else mimeType
        val fileSave = File("${Global.coverPaths}/${Tools.getRandomString()}.$mime")
        fileSave.createNewFile()
        return try {
            val stream = FileOutputStream(fileSave.absolutePath)
            stream.write(array)
            fileSave.absolutePath
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun copyFile (inputStream: InputStream?, target : String,callback: (Float) -> Unit) {
        File(target).createNewFile()
        val out = FileOutputStream(target)
        val lengthOfFile = inputStream!!.available()
        val buf = ByteArray(512)
        var len: Int
        var progress = 0
        while (inputStream.read(buf).also { len = it } != -1) {
            progress += len
            callback.invoke((progress * 100f / lengthOfFile) / 100f)
            out.write(buf, 0, len)
        }
        inputStream.close()
        out.close()
    }

    object UnzipUtils {
        @Throws(IOException::class)
        fun unzip(zipFilePath: File, destDirectory: String,callback: (Float) -> Unit) {
            var progress = 0
            var max: Int
            ZipFile(zipFilePath).use { zip ->
                max = zip.entries().asSequence().count()
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                        progress++
                        val filePath = destDirectory + File.separator + entry.name
                        if (!entry.isDirectory) {
                            extractFile(input, filePath)
                        } else {
                            val dir = File(filePath)
                            dir.mkdir()
                        }
                        callback.invoke((progress * 100f / max) / 100f)
                    }
                }
            }
        }
        @Throws(IOException::class)
        private fun extractFile(inputStream: InputStream, destFilePath: String) {
            val bos = BufferedOutputStream(FileOutputStream(destFilePath))
            val bytesIn = ByteArray(4096)
            var read: Int
            while (inputStream.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
            bos.close()
        }
    }

}