package happy.jyc.logger.save

import android.content.Context
import happy.jyc.logger.upload.LogEntity

interface ISave {
    /**
     * store the log to specific place
     *
     * @param context application context
     * @param entity log entity
     */
    fun save(context: Context, entity: LogEntity)

    /**
     * provide log entity to upload
     *
     * @param context application context
     */
    fun provide(context: Context): List<LogEntity>

    /**
     * provide log count to check is log should be upload
     *
     * @param context application context
     */
    fun provideCount(context: Context): Int

    /**
     * update the upload time
     *
     * @param timeStamp the timestamp(millisecond) of this time upload
     */
    fun updateUploadTime(context: Context, timeStamp: Long)

    /**
     * provide the timestamp(millisecond) of last time upload
     */
    fun provideUploadTime(context: Context): Long

    /**
     * clear the log in specific place
     *
     * @param context application context
     */
    fun clear(context: Context)
}