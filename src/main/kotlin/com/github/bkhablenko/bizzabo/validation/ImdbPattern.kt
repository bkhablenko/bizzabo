package com.github.bkhablenko.bizzabo.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.RUNTIME)
@Pattern(regexp = "^tt\\d{7}$")
@Constraint(validatedBy = [])
annotation class ImdbPattern(

    val message: String = "{com.github.bkhablenko.bizzabo.validation.ImdbPattern}",

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<out Payload>> = [],
)
