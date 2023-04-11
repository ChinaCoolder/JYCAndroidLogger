package happy.jyc.logger.save

import android.content.Context
import android.util.Log
import happy.jyc.logger.BuildConfig
import happy.jyc.logger.JYCLogger
import happy.jyc.logger.ext.trylock
import happy.jyc.logger.upload.LogEntity
import java.io.BufferedInputStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock

internal class PrivateStorageSaver: ISave, Serializable {
    companion object {
        private const val PATH = "jyclogger"
        private const val FILE_NAME = "jyclogger"
        private const val UPLOAD_TIME = "jyclogger_uploadtime"
    }

    override fun save(context: Context, entity: LogEntity) {
        val logFile = getLogFile(context)
        if (
            if (!logFile.exists()) {
                logFile.createNewFile()
            } else true
        ) {
            RandomAccessFile(logFile, "rw").use { file ->
                file.channel.use { channel ->
                    channel.trylock {
                        if (file.length() == 0L) {
                            file.seek(0)
                            file.writeInt(1)
                        } else {
                            file.seek(0)
                            val count = file.readInt()
                            file.seek(0)
                            file.writeInt(count + 1)
                        }
                        file.seek(file.length())
                        entity.title.toByteArray().let {
                            file.writeInt(it.size)
                            file.write(it)
                        }
                        entity.message.toByteArray().let {
                            file.writeInt(it.size)
                            file.write(it)
                        }
                    }
                }
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(JYCLogger.LOGCAT_TITLE, "log file create failed")
            }
        }
    }

    override fun provide(context: Context): List<LogEntity> {
        val result = mutableListOf<LogEntity>()
        if (getLogFile(context).exists()) {
            RandomAccessFile(getLogFile(context), "rw").use { file ->
                file.channel.use { channel ->
                    channel.trylock {
                        val count = file.readInt()
                        var title = ""
                        for (i in 1 .. (count * 2)) {
                            ByteArray(file.readInt()).apply {
                                file.read(this)
                                if (i % 2 == 1) {
                                    title = this.toString(Charsets.UTF_8)
                                } else {
                                    result.add(
                                        LogEntity(
                                            title,
                                            this.toString(Charsets.UTF_8)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    override fun provideCount(context: Context): Int {
        var count = 0
        if (getLogFile(context).exists()) {
            RandomAccessFile(getLogFile(context), "rw").use { file ->
                file.channel.use { channel ->
                    channel.trylock {
                        count = file.readInt()
                    }
                }
            }
        }
        return count
    }

    override fun clear(context: Context) {
        val file = getLogFile(context)
        RandomAccessFile(file, "rw").use { output ->
            output.channel.use { channel ->
                channel.trylock {
                    file.delete()
                }
            }
        }
    }

    override fun provideUploadTime(context: Context): Long {
        var result = 0L
        if (context.fileList().find { it == UPLOAD_TIME } != null) {
            context.openFileInput(UPLOAD_TIME).bufferedReader().use {
                result = it.readLine().toLong()
            }
        }
        return result
    }

    override fun updateUploadTime(context: Context, timeStamp: Long) {
        context.deleteFile(UPLOAD_TIME)
        context.openFileOutput(UPLOAD_TIME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write("$timeStamp")
        }
    }

    private fun getLogFile(context: Context) =
        File(
            "${context.getDir(PATH, Context.MODE_PRIVATE).path}${File.separator}${FILE_NAME}"
        )
}