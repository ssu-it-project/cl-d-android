package com.seumulseumul.cld.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.RefreshToken
import com.seumulseumul.domain.usecase.GetClimeRecordUseCase
import com.seumulseumul.domain.usecase.PostRefreshAuthTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRefreshAuthTokenUseCase: PostRefreshAuthTokenUseCase,
    private val getClimeRecordUseCase: GetClimeRecordUseCase
) : ViewModel() {

    private val _recordsSharedFlow = MutableSharedFlow<ClimeRecords>()
    val recordsSharedFlow = _recordsSharedFlow.asSharedFlow()

    private val _refreshAuthTokenSharedFlow = MutableSharedFlow<AuthToken>()
    val refreshAuthTokenSharedFlow = _refreshAuthTokenSharedFlow.asSharedFlow()

    fun postRefreshAuthToken(
        refreshTokenBody: RefreshToken
    ) = viewModelScope.launch {
        postRefreshAuthTokenUseCase
            .invoke(refreshTokenBody)
            .catch {
                it.printStackTrace()
            }.collect {
                _refreshAuthTokenSharedFlow.emit(it)
            }
    }

    fun getClimeRecords(
        auth: String,
        limit: Int = 5,
        skip: Int = 0
    ) = viewModelScope.launch {
        getClimeRecordUseCase
            .invokeGetClimeRecord(auth, limit, skip)
            .catch {
                it.printStackTrace()
            }.collect {
                _recordsSharedFlow.emit(it)
            }
    }
}