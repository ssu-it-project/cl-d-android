package com.seumulseumul.cld.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.usecase.GetClimeRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getClimeRecordUseCase: GetClimeRecordUseCase
) : ViewModel() {

    private val _recordsSharedFlow = MutableSharedFlow<ClimeRecords>()
    val recordsSharedFlow = _recordsSharedFlow.asSharedFlow()

    fun getClimeRecords(
        auth: String,
        limit: Int = 5,
        skip: Int = 0
    ) = viewModelScope.launch {
        getClimeRecordUseCase
            .invoke(auth, limit, skip)
            .catch {
                it.printStackTrace()
            }.collect {
                _recordsSharedFlow.emit(it)
            }
    }
}