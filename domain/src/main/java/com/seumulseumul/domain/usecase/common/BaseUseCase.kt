package com.seumulseumul.domain.usecase.common

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<T : Any, R : Any> {
    operator fun invoke(param: T): Flow<R>
}