package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.ClimbingGym
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClimbingGymDetailUseCase @Inject constructor(
    private val repository: ClDRepository
){
    fun invoke(
        auth: String,
        id: String
    ): Flow<ClimbingGym> = repository.getClimbingGymDetail(auth, id)
}