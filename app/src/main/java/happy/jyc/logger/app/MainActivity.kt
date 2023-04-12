package happy.jyc.logger.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import happy.jyc.logger.JYCLogger
import happy.jyc.logger.policy.IPolicy
import happy.jyc.logger.policy.NumberPolicy
import happy.jyc.logger.policy.TimePolicy
import happy.jyc.logger.save.ISave
import happy.jyc.logger.upload.LogEntity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JYCLogger.Builder(applicationContext, Upload::class.java)
            .policy(
                TimePolicy.minutes(30)
            )
            .build()
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
        JYCLogger.INSTANCE.log("test", "test")
    }
}