package com.begletsov.redbook.models

data class Description(var name: String,
                       var title: String,
                       var descriptionText: String,
                       val imagePath: String,
                       val audioFilePath: String)