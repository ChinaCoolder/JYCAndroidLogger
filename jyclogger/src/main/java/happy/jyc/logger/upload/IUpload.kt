package happy.jyc.logger.upload

import android.content.Context
import android.util.Log

interface IUpload {
    /**
     * upload function, this should implement by user
     *
     * @param ctx application context
     * @param logs the log list waiting for upload
     */
    fun upload(ctx: Context, logs: List<LogEntity>): Boolean
}