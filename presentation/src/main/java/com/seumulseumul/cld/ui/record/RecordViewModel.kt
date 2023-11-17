package com.seumulseumul.cld.ui.record

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.usecase.PostClimeRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val postClimeRecordUseCase: PostClimeRecordUseCase
): ViewModel() {
    private val _climeRecordSharedFlow = MutableSharedFlow<Boolean>()
    val climeRecordSharedFlow = _climeRecordSharedFlow.asSharedFlow()

    fun postClimeRecord(
        auth: String,
        climbingGymId: String,
        content: String,
        sector: String,
        level: String,
        resolution: String,
        video: File,
    ) = viewModelScope.launch {
        postClimeRecordUseCase.invoke(
            auth, climbingGymId, content, sector, level, resolution, video
        ).catch {
            it.printStackTrace()
            _climeRecordSharedFlow.emit(false)
        }.collect {
            _climeRecordSharedFlow.emit(true)
        }
    }
}