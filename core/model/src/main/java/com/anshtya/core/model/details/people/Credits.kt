package com.anshtya.core.model.details.people

import com.anshtya.core.model.details.Cast

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>
)
