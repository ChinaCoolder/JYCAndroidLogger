package happy.jyc.logger.app

import android.content.Context
import android.util.Log
import happy.jyc.logger.upload.IUpload
import happy.jyc.logger.upload.LogEntity

class Upload: IUpload {
    override fun upload(ctx: Context, logs: List<LogEntity>): Boolean {
        logs.forEachIndexed { index, logEntity ->
            Log.e("UploadEntityTitle", "$index")
            Log.e("UploadEntityTitle", logEntity.title)
            Log.e("UploadEntityMessage", logEntity.message)
        }
        return true
    }
}