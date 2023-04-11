package happy.jyc.logger.policy

import java.io.Serializable

class NumberPolicy(
    private val number: Int
): IPolicy, Serializable {

    override fun pass(count: Int, lastTimeUpload: Long): Boolean =
        count >= number
}