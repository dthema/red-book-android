package com.begletsov.redbook.models

import java.util.UUID

data class Place(val id: UUID = UUID.randomUUID(),
                 val description: Description,
                 val categoryId: UUID,
                 val location: Geopoint)