package com.example.starwars.util.ext


fun String.getIdsFromUrl(): String {
    return this.split("/").last { it.isNotEmpty() }
}
