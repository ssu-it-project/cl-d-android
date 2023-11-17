package com.seumulseumul.domain.usecase

import android.net.Uri
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PostClimeRecordUseCase @Inject constructor(
    private val repository: ClDRepository
) {
    fun invoke(
        auth: String,
        climbingGymId: String,
        content: String,
        sector: String,
        level: String,
        resolution: String,
        video: File,
    ): Flow<Any> = repository.postClimeRecord(auth, climbingGymId, content, sector, level, resolution, video)
}