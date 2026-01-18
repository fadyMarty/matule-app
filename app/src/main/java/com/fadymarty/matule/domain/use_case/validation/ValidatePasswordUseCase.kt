package com.fadymarty.matule.domain.use_case.validation

class ValidatePasswordUseCase() {
    operator fun invoke(password: String): Boolean {
        if (password.length < 8) {
            return false
        }
        if (!password.any { it.isLowerCase() }) {
            return false
        }
        if (!password.any { it.isUpperCase() }) {
            return false
        }
        if (!password.any { it.isDigit() }) {
            return false
        }
        if (!password.any { it.isLetter() }) {
            return false
        }
        if (!password.any { it.isWhitespace() }) {
            return false
        }
        if (!password.any { !it.isLetterOrDigit() && !it.isWhitespace() }) {
            return false
        }
        return true
    }
}