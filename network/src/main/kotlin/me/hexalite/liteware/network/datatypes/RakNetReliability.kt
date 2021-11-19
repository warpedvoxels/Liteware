package me.hexalite.liteware.network.datatypes

enum class RakNetReliability(
    val isReliable: Boolean,
    val isOrdered: Boolean,
    val isSequenced: Boolean,
    val hasAckReceipt: Boolean
) {
    UNRELIABLE(false, false, false, false),
    UNRELIABLE_SEQUENCED(false, false, true, false),
    RELIABLE(true, false, false, false),
    RELIABLE_ORDERED(true, true, false, false),
    RELIABLE_SEQUENCED(true, false, true, false),
    UNRELIABLE_WITH_ACK_RECEIPT(false, false, false, true),
    RELIABLE_WITH_ACK_RECEIPT(true, false, false, true),
    RELIABLE_ORDERED_WITH_ACK_RECEIPT(true, true, false, true);

    companion object {
        operator fun get(id: Int) = values().getOrNull(id) ?: UNRELIABLE
    }

}