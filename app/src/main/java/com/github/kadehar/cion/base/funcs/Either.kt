package com.github.kadehar.cion.base.funcs

sealed class Either<out LEFT, out RIGHT> {

    class Left<LEFT>(val value: LEFT) : Either<LEFT, Nothing>()
    class Right<RIGHT>(val value: RIGHT) : Either<Nothing, RIGHT>()

    val isRight: Boolean
        get() = this is Right

    val isLeft: Boolean
        get() = this is Left

    inline fun <TYPE> fold(onError: (LEFT) -> TYPE, onSuccess: (RIGHT) -> TYPE): TYPE =
        when (this) {
            is Left -> onError(this.value)
            is Right -> onSuccess(this.value)
        }

    inline fun <TYPE> map(transform: (RIGHT) -> TYPE): Either<LEFT, TYPE> = when (this) {
        is Left -> Left(this.value)
        is Right -> Right(transform(this.value))
    }

    inline fun <TYPE> mapLeft(transform: (LEFT) -> TYPE): Either<TYPE, RIGHT> = when (this) {
        is Left -> Left(transform(value))
        is Right -> Right(this.value)
    }
}

inline fun <TYPE, LEFT, RIGHT> Either<LEFT, RIGHT>.flatMap(
    transform: (RIGHT) -> Either<LEFT, TYPE>
): Either<LEFT, TYPE> {
    return when (this) {
        is Either.Left -> Either.Left(value)
        is Either.Right -> transform(value)
    }
}