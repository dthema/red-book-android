package com.begletsov.redbook.models

import java.util.UUID

data class Category(val id: UUID,
                    var name: String,
                    val iconFilePath: String)
