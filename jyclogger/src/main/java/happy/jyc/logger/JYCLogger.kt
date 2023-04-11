package happy.jyc.logger

import android.content.Context
import android.util.Log
import androidx.work.*
import happy.jyc.logger.policy.IPolicy
import happy.jyc.logger.save.ISave
import happy.jyc.logger.save.PrivateStorageSaver
import happy.jyc.logger.upload.LogEntity
import happy.jyc.logger.upload.UploadWorker
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class JYCLogger private constructor() {
    companion object {
        const val LOGCAT_TITLE = "jyclogger"
        const val UPLOAD_FILE = "jyclogger_upload"
        const val SAVER_FILE = "jyclogger_save"
        const val POLICY_FILE = "jyclogger_policy"

        private const val WORKER_TAG = "jyclogger"

        val INSTANCE: JYCLogger by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            JYCLogger()
        }

        internal fun getSaver(context: Context): ISave {
            var result: ISave
            context.openFileInput(SAVER_FILE).use { input ->
                ObjectInputStream(input).use {
                    result = it.readObject() as ISave
                }
            }
            return result
        }

        internal fun getPolicy(context: Context): IPolicy? =
            if (context.fileList().find { it == POLICY_FILE } != null) {
                var result: IPolicy?
                context.openFileInput(POLICY_FILE).use { input ->
                    ObjectInputStream(input).use {
                        result = it.readObject() as IPolicy
                    }
                }
                result
            } else {
                null
            }
    }

    class Builder(
        private val context: Context,
        private val upload: Class<*>,
        private val save: ISave = PrivateStorageSaver()
    ) {
        fun policy(policy: IPolicy): Builder {
            context.deleteFile(POLICY_FILE)
            context.openFileOutput(POLICY_FILE, Context.MODE_PRIVATE).use { out ->
                ObjectOutputStream(out).use {
                    it.writeObject(policy)
                }
            }
            return this
        }

        fun build() = INSTANCE.apply {
            this.ctx = context
            this.saver = save
            context.deleteFile(UPLOAD_FILE)
            context.openFileOutput(UPLOAD_FILE, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(upload.name)
            }

            context.deleteFile(SAVER_FILE)
            context.openFileOutput(SAVER_FILE, Context.MODE_PRIVATE).use { out ->
                ObjectOutputStream(out).use {
                    it.writeObject(save)
                }
            }

            WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORKER_TAG, ExistingPeriodicWorkPolicy.UPDATE,
                    PeriodicWorkRequestBuilder<UploadWorker>(1, TimeUnit.HOURS).apply {
                        setConstraints(
                            Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        )
                    }.build()
                )
        }
    }

    private val logThreadPool = Executors.newSingleThreadExecutor()
    private var ctx: Context? = null
    private var saver: ISave? = null

    /**
     * log function, this will invoke {@link #saver#save(Context, LogEntity)}
     *
     * @param title the title of log, max byte length is {@link kotlin.Int#MAX_VALUE }
     * @param message the message of log, max byte length is {@link kotlin.Int#MAX_VALUE }
     */
    fun log(title: String, message: String) {
        Int.MAX_VALUE
        if (ctx != null) {
            logThreadPool.submit {
                ctx?.let {
                    saver?.save(it, LogEntity(title, message))
                }
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.i(
                    LOGCAT_TITLE,
                    "context is null"
                )
            }
        }
    }
}