package com.ifpr.androidapptemplate.baseclasses

data class TalesCampaign(
    var nome: String? = null,
    var type: String? = null,
    var description: String? = null,
    var base64Image: String? = null,
    val imageUrl: String? = null,
    var ownerUid: String? = null
)
