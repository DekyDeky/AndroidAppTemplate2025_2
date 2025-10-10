package com.ifpr.androidapptemplate.baseclasses

data class Item(
    var tarefa: String? = null,
    var descricao: String? = null,
    var dataInicio: String? = null,
    var dataFim: String? = null,
    val base64Image: String? = null,
    val imageUrl: String? = null
)
