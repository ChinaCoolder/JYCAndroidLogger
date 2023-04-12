package happy.jyc.logger.app

import android.content.Context
import android.util.Log
import happy.jyc.logger.upload.IUpload
import happy.jyc.logger.upload.LogEntity

class Upload: IUpload {
    override fun upload(ctx: Context, logs: List<LogEntity>): Boolean {

        return true
    }
}