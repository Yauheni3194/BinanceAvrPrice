package by.kovalevskiy.BinanceAvrPrice.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Bad Request");
        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null && requiredType.isAssignableFrom(Instant.class)) {
            errorResponse.put("message", String.format(
                    "Неверный формат даты в параметре '%s'. Значение '%s' не соответствует стандарту ISO-8601. Пример корректного формата: 2026-11-04T12:00:00Z",
                    ex.getName(), ex.getValue()
            ));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (requiredType != null && requiredType.isEnum()) {
            errorResponse.put("message", String.format(
                    "Значение '%s' не поддерживается для параметра '%s'. Допустимые тикеры: %s",
                    ex.getValue(), ex.getName(), Arrays.toString(requiredType.getEnumConstants())
            ));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        errorResponse.put("message", String.format("Параметр '%s' имеет неверный тип данных.", ex.getName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unprocessable Entity");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }
}
