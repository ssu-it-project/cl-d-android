package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.SignIn
import com.seumulseumul.domain.model.Term
import com.seumulseumul.domain.repository.ClDRepository
import com.seumulseumul.domain.usecase.common.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTermsUseCase @Inject constructor(
    private val repository: ClDRepository
): BaseUseCase<Any, List<Term>> {
    override fun invoke(param: Any): Flow<List<Term>> = repository.getTerms()
}