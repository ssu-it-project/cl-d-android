package com.seumulseumul.cld.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.Term
import com.seumulseumul.domain.usecase.GetTermsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getTermsUseCase: GetTermsUseCase
): ViewModel() {

    private val _termsSharedFlow = MutableSharedFlow<List<Term>>()
    val termsSharedFlow = _termsSharedFlow.asSharedFlow()

    fun getTerms() = viewModelScope.launch {
        getTermsUseCase.invoke(Any())
            .catch {
                it.printStackTrace()
            }
            .collect {
                _termsSharedFlow.emit(it)
            }
    }
}