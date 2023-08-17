package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.repository.ClDRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClimeRecordUseCase@Inject constructor(
    private val repository: ClDRepository
) {
    fun invoke(
        auth: String,
        limit: Int,
        skip: Int
    ): Flow<ClimeRecords> = repository.getClimeRecord(auth, limit, skip)
}