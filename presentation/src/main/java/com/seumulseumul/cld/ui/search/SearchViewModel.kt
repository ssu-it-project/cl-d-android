package com.seumulseumul.cld.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.usecase.ClimbingGymBookmarkUseCase
import com.seumulseumul.domain.usecase.GetClimbingGymsUseCase
import com.seumulseumul.domain.usecase.GetGymBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getClimeGymsUseCase: GetClimbingGymsUseCase,
    private val getGymBookmarkUseCase: GetGymBookmarkUseCase
) : ViewModel() {
    private val _climbingGymsSharedFlow = MutableSharedFlow<Gyms>()
    val climbingGymsSharedFlow = _climbingGymsSharedFlow.asSharedFlow()

    private val _climbingGymsBookmarkSharedFlow = MutableSharedFlow<Gyms>()
    val climbingGymsBookmarkSharedFlow = _climbingGymsBookmarkSharedFlow.asSharedFlow()

    val searchKeywordLiveData = MutableLiveData<String>()

    fun getClimbingGyms(
        auth: String,
        x: Double,
        y: Double,
        keyword: String,
        limit: Int = 10,
        skip: Int = 0
    ) = viewModelScope.launch {
        getClimeGymsUseCase
            .invoke(auth, x, y, limit, skip, keyword)
            .catch {
                it.printStackTrace()
            }.collect {
                _climbingGymsSharedFlow.emit(it)
            }
    }

    fun getClimbingGymsBookmark(
        auth: String,
        keyword: String,
        limit: Int = 10,
        skip: Int = 0
    ) = viewModelScope.launch {
        getGymBookmarkUseCase.invoke(
            auth, keyword, limit, skip
        ).catch {
            it.printStackTrace()
        }.collect {
            _climbingGymsBookmarkSharedFlow.emit(it)
        }
    }
}