package me.hexalite.liteware.network.datatypes

enum class RakNetReliability(
    val reliable: Boolean,
    val ordered: Boolean,
    val sequenced: Boolean,
    val withAckReceipt: Boolean
) {
    UNRELIABLE(false, false, false, false),
    UNRELIABLE_SEQUENCED(false, false, true, false),
    RELIABLE(true, false, false, false),
    RELIABLE_ORDERED(true, true, false, false),
    RELIABLE_SEQUENCED(true, false, true, false),
    UNRELIABLE_WITH_ACK_RECEIPT(false, false, false, true),
    RELIABLE_WITH_ACK_RECEIPT(true, false, false, true),
    RELIABLE_ORDERED_WITH_ACK_RECEIPT(true, true, false, true);

    val size = run {
        var size = 0
        if (reliable) {
            size += 3
        }
        if (sequenced) {
            size += 3
        }
        if (ordered) {
            size += 4
        }
        size
    }

    companion object {
        operator fun get(id: Int) = values().getOrNull(id) ?: UNRELIABLE
    }

}