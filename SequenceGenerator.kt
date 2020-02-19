package util

import com.electronslab.util.IdGenerator
import java.net.NetworkInterface
import java.security.SecureRandom
import java.time.Instant

class SequenceGenerator:IdGenerator {
    override fun id(): Long  = this.nextId()


    private val nodeId: Int

    @Volatile
    private var lastTimestamp = -1L
    @Volatile
    private var sequence = 0L

    // Create SequenceGenerator with a nodeId
    constructor(nodeId: Int) {
        require(!(nodeId < 0 || nodeId > maxNodeId)) {
            String.format(
                "NodeId must be between %d and %d",
                0,
                maxNodeId
            )
        }
        this.nodeId = nodeId
    }

    // Let SequenceGenerator generate a nodeId
    constructor() {
        this.nodeId = createNodeId()
    }


    @Synchronized
    fun nextId(): Long {
        var currentTimestamp = timestamp()

        check(currentTimestamp >= lastTimestamp) { "Invalid System Clock!" }

        if (currentTimestamp == lastTimestamp) {
            sequence = sequence + 1 and maxSequence
            if (sequence == 0L) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            // reset sequence to start with zero for the next millisecond
            sequence = 0
        }

        lastTimestamp = currentTimestamp

        var id = currentTimestamp shl TOTAL_BITS - EPOCH_BITS
        id = id or (nodeId shl TOTAL_BITS - EPOCH_BITS - NODE_ID_BITS).toLong()
        id = id or sequence
        return id
    }

    // Block and wait till next millisecond
    private fun waitNextMillis(currentTimeStamp: Long): Long {
        var currentTimestamp = currentTimeStamp
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp()
        }
        return currentTimestamp
    }

    private fun createNodeId(): Int {
        var nodeId: Int
        try {
            val sb = StringBuilder()
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val mac = networkInterface.getHardwareAddress()
                if (mac != null) {
                    for (i in mac.indices) {
                        sb.append(String.format("%02X", mac[i]))
                    }
                }
            }
            nodeId = sb.toString().hashCode()
        } catch (ex: Exception) {
            nodeId = SecureRandom().nextInt()
        }

        nodeId = nodeId and maxNodeId
        return nodeId
    }

    companion object {
        private val TOTAL_BITS = 64
        private val EPOCH_BITS = 42
        private val NODE_ID_BITS = 10
        private val SEQUENCE_BITS = 12

        private val maxNodeId = (Math.pow(2.0, NODE_ID_BITS.toDouble()) - 1).toInt()
        private val maxSequence = (Math.pow(2.0, SEQUENCE_BITS.toDouble()) - 1).toLong()

        // Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
        private val CUSTOM_EPOCH = 1420070400000L


        private val generator =  SequenceGenerator()
        fun nextId() = generator.nextId()

        // Get current timestamp in milliseconds, adjust for the custom epoch.
        private fun timestamp(): Long {
            return Instant.now().toEpochMilli() - CUSTOM_EPOCH
        }
    }
}