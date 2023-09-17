package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClimbingGymBookmarkUseCase @Inject constructor(
    private val repository: ClDRepository
) {
    fun postInvoke(
        auth: String,
        id: String
    ): Flow<Any> = repository.postClimbingGymBookmark(auth, id)

    fun deleteInvoke(
        auth: String,
        id: String
    ): Flow<Any> = repository.deleteClimbingGymBookmark(auth, id)
}