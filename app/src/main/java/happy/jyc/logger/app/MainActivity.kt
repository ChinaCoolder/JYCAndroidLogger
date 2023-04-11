package happy.jyc.logger.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import happy.jyc.logger.JYCLogger
import happy.jyc.logger.policy.NumberPolicy

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JYCLogger.Builder(applicationContext, Upload::class.java)
            .policy(
                NumberPolicy(10)
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