package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.SignUp
import com.seumulseumul.domain.repository.ClDRepository
import com.seumulseumul.domain.usecase.common.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostSignUpUseCase @Inject constructor(
    private val repository: ClDRepository
): BaseUseCase<SignUp, AuthToken> {
    override fun invoke(param: SignUp): Flow<AuthToken> = repository.postSignUp(param)
}