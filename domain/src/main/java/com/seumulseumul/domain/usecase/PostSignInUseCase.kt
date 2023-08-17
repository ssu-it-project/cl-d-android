package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.SignIn
import com.seumulseumul.domain.repository.ClDRepository
import com.seumulseumul.domain.usecase.common.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostSignInUseCase @Inject constructor(
    private val repository: ClDRepository
): BaseUseCase<SignIn, AuthToken> {
    override fun invoke(param: SignIn): Flow<AuthToken> = repository.postSignIn(param)
}