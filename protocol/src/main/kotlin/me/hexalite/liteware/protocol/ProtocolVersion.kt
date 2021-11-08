package me.hexalite.liteware.protocol

enum class ProtocolVersion(val protocolId: Int) {

    Bedrock_1_16_0_to_1_16_10(407),
    Bedrock_1_16_20(408),
    Bedrock_1_16_100(419),
    Bedrock_1_16_200_to_1_16_201(422),
    Bedrock_1_16_210(428),
    Bedrock_1_16_220(431),
    Bedrock_1_17_0(440),
    Bedrock_1_17_10_to_1_17_11(448),
    Bedrock_1_17_30_to_1_17_34(465),
    Bedrock_1_17_40_to_1_17_41(471);

    companion object {
        operator fun get(protocolId: Int) = values().firstOrNull { it.protocolId == protocolId }
    }

}