package com.seumulseumul.cld.ui.gym

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.ClimbingGym
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.usecase.ClimbingGymBookmarkUseCase
import com.seumulseumul.domain.usecase.GetClimbingGymDetailUseCase
import com.seumulseumul.domain.usecase.GetClimeRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymDetailViewModel @Inject constructor(
    private val getClimbingGymDetailUseCase: GetClimbingGymDetailUseCase,
    private val climbingGymBookmarkUseCase: ClimbingGymBookmarkUseCase,
    private val getClimeRecordUseCase: GetClimeRecordUseCase
): ViewModel() {

    companion object {
        private val TAG: String = GymDetailViewModel::class.java.simpleName
    }

    private val _gymDetailSharedFlow = MutableSharedFlow<ClimbingGym>()
    val gymDetailSharedFlow = _gymDetailSharedFlow.asSharedFlow()

    private val _titleRecordSharedFlow = MutableSharedFlow<ClimeRecords>()
    val titleRecordSharedFlow = _titleRecordSharedFlow.asSharedFlow()

    fun getClimbingGymDetail(
        auth: String,
        id: String
    ) = viewModelScope.launch {
        getClimbingGymDetailUseCase.invoke(auth, id)
            .catch {
                it.printStackTrace()
            }.collect {
                _gymDetailSharedFlow.emit(it)
            }
    }

    fun postClimbingGymBookmark(
        auth: String,
        id: String
    ) = viewModelScope.launch {
        climbingGymBookmarkUseCase.postInvoke(auth, id)
            .catch {
                it.printStackTrace()
            }.collect {
                Log.d(TAG, "[postClimbingGymBookmark] success")
            }
    }

    fun deleteClimbingGymBookmark(
        auth: String,
        id: String
    ) = viewModelScope.launch {
        climbingGymBookmarkUseCase.deleteInvoke(auth, id)
            .catch {
                it.printStackTrace()
            }.collect {
                Log.d(TAG, "[deleteClimbingGymBookmark] success")
            }
    }

    fun getTitleClimeRecord(
        auth: String,
        id: String,
    ) = viewModelScope.launch {
        getClimeRecordUseCase.invokeGetClimeGymRecords(
            auth,
            id,
            "",
            1,
            0
        ).catch {
            it.printStackTrace()
        }.collect {
            _titleRecordSharedFlow.emit(it)
        }
    }
}