package com.ifpr.androidapptemplate.baseclasses

import java.io.Serializable

data class TalesCampaign(
    var nome: String? = null,
    var type: String? = null,
    var description: String? = null,
    var code: String? = null,
    var base64Image: String? = null,
    val imageUrl: String? = null,
    var ownerUid: String? = null,
    var jogadores: MutableMap<String, MutableMap<String, Boolean>> = mutableMapOf()
) : Serializable
