package happy.jyc.logger.policy

interface IPolicy {
    /**
     * check is log should be upload
     *
     * @param count the log count currently stored
     * @param lastTimeUpload the timestamp(millisecond) last time upload the log
     *
     * @return true if this policy is passed, otherwise is false
     */
    fun pass(count: Int, lastTimeUpload: Long): Boolean
}