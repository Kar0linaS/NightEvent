package com.dziubi.nolio.data.models

data class EventModel(
    var id: String? = null,
    val userId: String? = null,
    val desc: String? = null,
    val  location: LocationModel? = null,
    var imageUrl: String? = null,
    val reactions: Int? = null
)
