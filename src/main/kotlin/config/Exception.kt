data class ErrorResponse(val code: String, val field: String? = null)

data class BadRequestException(val error: ErrorResponse) : RuntimeException("400 Bad Request: $error")

data class NotFoundException(val error: ErrorResponse) : RuntimeException("404 Not Found: $error")

data class InternalServerException(val error: ErrorResponse) : RuntimeException("500 Not Found: $error")