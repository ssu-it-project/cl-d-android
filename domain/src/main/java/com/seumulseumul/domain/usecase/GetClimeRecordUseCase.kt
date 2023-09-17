package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClimeRecordUseCase@Inject constructor(
    private val repository: ClDRepository
) {
    fun invokeGetClimeRecord(
        auth: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords> = repository.getClimeRecord(auth, limit, skip)

    fun invokeGetClimeGymRecords(
        auth: String,
        id: String,
        keyword: String,
        limit: Int,
        skip: Int,
    ): Flow<ClimeRecords> = repository.getClimbingGymRecords(auth, id, keyword, limit, skip)
}