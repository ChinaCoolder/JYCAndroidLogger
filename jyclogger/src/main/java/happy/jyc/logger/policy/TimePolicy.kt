package happy.jyc.logger.policy

import java.io.Serializable

class TimePolicy private constructor(
    private val time: Long
): IPolicy, Serializable {
    private constructor() : this(0)

    companion object {
        fun days(count: Int) =
            TimePolicy(
                count * 24 * 60 * 60 * 1000L
            )

        fun hours(count: Int) =
            TimePolicy(
                count * 60 * 60 * 1000L
            )

        fun minutes(count: Int) =
            TimePolicy(
                count * 60 * 1000L
            )
    }
    override fun pass(count: Int, lastTimeUpload: Long): Boolean =
        System.currentTimeMillis() - lastTimeUpload >= time
}