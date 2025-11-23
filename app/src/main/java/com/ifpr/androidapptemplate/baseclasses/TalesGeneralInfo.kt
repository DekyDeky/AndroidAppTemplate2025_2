package com.ifpr.androidapptemplate.baseclasses

import java.io.Serializable

data class TalesGeneralInfo(
    var name: String = "", // nome não pode ser null
    var type: String = "", // tipo não pode ser null
    var age: Int = 0, // valor padrão 0, não permite null
    var luckPoints: Int = 0, // valor padrão 0
    var drive: String = "", // valor default
    var anchor: String = "", // valor default
    var problem: String = "", // valor default
    var pride: String = "", // valor default
    var description: String = "", // valor default
    var song: String = "", // valor default
    val relationships: String = "",
    val hideout: String = "",
    var attributes: TalesAttributes? = null, // Permite null, pois pode ser opcional
    var skills: TalesSkills? = null, // Permite null
    var items: TalesItems? = null, // Permite null
    val base64Image: String? = null,
    val imageUrl: String? = null,
    var conditions: TalesConditions? = null,
    var experience: Int? = 0,
    var prideCheck: Boolean = false
) : Serializable