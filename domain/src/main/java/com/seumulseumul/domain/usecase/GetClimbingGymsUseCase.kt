package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClimbingGymsUseCase @Inject constructor(
    private val repository: ClDRepository
) {
    fun invoke(
        auth: String,
        limit: Int,
        skip: Int,
        keyword: String
    ): Flow<Gyms> = repository.getClimbingGyms(auth, limit, skip, keyword)
}