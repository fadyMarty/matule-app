package com.fadymarty.matule.domain.use_case.validation

class ValidatePasswordConfirmUseCase() {
    operator fun invoke(password: String, passwordConfirm: String): Boolean {
        return password == passwordConfirm
    }
}