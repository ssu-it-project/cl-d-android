package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGymBookmarkUseCase @Inject constructor(
    private val repository: ClDRepository
) {
    fun invoke(
        auth: String,
        keyword: String,
        limit: Int,
        skip: Int,
    ): Flow<Gyms> = repository.getClimbingGymBookmark(auth, keyword, limit, skip)
}