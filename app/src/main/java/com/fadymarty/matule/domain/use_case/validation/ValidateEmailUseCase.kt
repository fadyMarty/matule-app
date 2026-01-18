package com.fadymarty.matule.domain.use_case.validation

class ValidateEmailUseCase {
    operator fun invoke(email: String): Boolean {
        val emailPattern = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]+$")
        return emailPattern.matches(email)
    }
}