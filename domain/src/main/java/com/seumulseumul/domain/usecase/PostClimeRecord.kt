package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class PostClimeRecord @Inject constructor(
    private val repository: ClDRepository
) {
    fun invoke(
        auth: String,
        climbingGymId: String,
        content: String,
        sector: String,
        level: String,
        video: File
    ): Flow<Any> = repository.postClimeRecord(auth, climbingGymId, content, sector, level, video)
}