package happy.jyc.logger.ext

import java.nio.channels.FileChannel
import java.nio.channels.FileLock

internal fun FileChannel.trylock(
    block: () -> Unit
) {
    var fileLock: FileLock?
    while (this.tryLock().also { fileLock = it } != null) {
        try {
            block.invoke()
        } finally {
            fileLock?.release()
            break
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun<R> String.invoke(functionName: String, vararg args: Any): R {
    val clazz = Class.forName(this)
    val method = clazz.methods.find { it.name == functionName }
    if (method != null) {
        return method.invoke(clazz.newInstance(), *args) as R
    } else {
        throw java.lang.IllegalStateException("Method not found")
    }
}