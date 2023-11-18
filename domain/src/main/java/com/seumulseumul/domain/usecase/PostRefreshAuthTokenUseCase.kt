package com.seumulseumul.domain.usecase

import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.RefreshToken
import com.seumulseumul.domain.repository.ClDRepository
import com.seumulseumul.domain.usecase.common.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRefreshAuthTokenUseCase @Inject constructor(
    private val repository: ClDRepository
): BaseUseCase<RefreshToken, AuthToken> {
    override fun invoke(param: RefreshToken): Flow<AuthToken> = repository.postAuthRefreshToken(param)
}