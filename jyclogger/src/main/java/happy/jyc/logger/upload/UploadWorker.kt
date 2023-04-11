package happy.jyc.logger.upload

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import happy.jyc.logger.BuildConfig
import happy.jyc.logger.JYCLogger
import happy.jyc.logger.ext.invoke
import happy.jyc.logger.policy.IPolicy
import happy.jyc.logger.save.ISave
import java.io.FileNotFoundException

class UploadWorker(
    context: Context,
    workParams: WorkerParameters
): Worker(context, workParams) {
    override fun doWork(): Result {
        try {
            val saver = JYCLogger.getSaver(applicationContext)
            if (
                JYCLogger.getPolicy(applicationContext)?.pass(
                    saver.provideCount(applicationContext),
                    saver.provideUploadTime(applicationContext)
                ) == false
            ) {
                if (BuildConfig.DEBUG) {
                    Log.i(JYCLogger.LOGCAT_TITLE, "Don't compliance with upload policy, skip upload process")
                }
                return Result.failure()
            }
            var success: Boolean
            applicationContext.openFileInput(JYCLogger.UPLOAD_FILE).bufferedReader().use {
                success = it.readLine().invoke(
                    IUpload::upload.name, applicationContext, saver.provide(applicationContext)
                )
            }
            if (success) {
                saver.clear(applicationContext)
                saver.updateUploadTime(applicationContext, System.currentTimeMillis())
            }
            return if (success) Result.success() else Result.failure()
        } catch (e: FileNotFoundException) {
            if (BuildConfig.DEBUG) {
                Log.e(JYCLogger.LOGCAT_TITLE, "Uploader define file not found")
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(JYCLogger.LOGCAT_TITLE, "Unexpected error:")
                Log.e(JYCLogger.LOGCAT_TITLE, e.message.orEmpty())
            }
        }
        return Result.failure()
    }
}