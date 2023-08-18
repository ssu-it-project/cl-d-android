package com.seumulseumul.cld.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.SignUp
import com.seumulseumul.domain.model.Term
import com.seumulseumul.domain.usecase.GetTermsUseCase
import com.seumulseumul.domain.usecase.PostSignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getTermsUseCase: GetTermsUseCase,
    private val postSingUpUseCase: PostSignUpUseCase
): ViewModel() {

    private val _termsSharedFlow = MutableSharedFlow<List<Term>>()
    val termsSharedFlow = _termsSharedFlow.asSharedFlow()

    private val _signUpSharedFlow = MutableSharedFlow<AuthToken>()
    val signUpSharedFlow = _signUpSharedFlow.asSharedFlow()

    fun getTerms() = viewModelScope.launch {
        getTermsUseCase.invoke(Any())
            .catch {
                it.printStackTrace()
            }
            .collect {
                _termsSharedFlow.emit(it)
            }
    }

    fun postSignUp(signUp: SignUp) = viewModelScope.launch {
        postSingUpUseCase.invoke(signUp)
            .catch {
                it.printStackTrace()
            }
            .collect{
                _signUpSharedFlow.emit(it)
            }
    }
}