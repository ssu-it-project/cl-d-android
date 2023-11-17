package com.seumulseumul.cld.ui.record.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.usecase.GetClimbingGymsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordGymViewModel @Inject constructor(
    private val getClimeGymsUseCase: GetClimbingGymsUseCase
): ViewModel() {
    private val _climbingGymsSharedFlow = MutableSharedFlow<Gyms>()
    val climbingGymsSharedFlow = _climbingGymsSharedFlow.asSharedFlow()

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
}