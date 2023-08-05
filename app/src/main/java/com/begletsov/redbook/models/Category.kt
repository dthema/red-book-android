package com.begletsov.redbook.models

import java.util.ArrayList
import java.util.UUID

data class Category(val id: UUID = UUID.randomUUID(),
                    val name: String,
                    val iconFilePath: Int,
                    val places: ArrayList<Place> = ArrayList())
