package com.ajaxproject.warehouse.controller

import com.ajaxproject.warehouse.exception.NotFoundException
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFound(notFoundException: NotFoundException): ErrorResponse {
        return ErrorResponse(status = HttpStatus.NOT_FOUND.value(),
            message = HttpStatus.NOT_FOUND.reasonPhrase,
            error = notFoundException.message,
            errorList = notFoundException.errorList)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleNotValidArgs(e: MethodArgumentNotValidException): ErrorResponse {
        val errors: List<String> = e.bindingResult.fieldErrors.asSequence()
            .map { it.defaultMessage }
            .filterNotNull()
            .toList()
        return ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = HttpStatus.BAD_REQUEST.reasonPhrase,
            errorList = errors
        )
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    fun handleSQLIntegrityConstraintViolationException(e: SQLIntegrityConstraintViolationException): ErrorResponse {
        return ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            message = HttpStatus.CONFLICT.reasonPhrase,
            error = e.message
        )
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ErrorResponse(
        val status: Int,
        val message: String?,
        val error: String? = null,
        val errorList: List<String>? = null
    )
}
