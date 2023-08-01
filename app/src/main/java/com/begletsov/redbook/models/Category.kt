package com.begletsov.redbook.models

import java.util.UUID

data class Category(
    var id: UUID,
    var name: String,
    var iconFilePath: String
)
