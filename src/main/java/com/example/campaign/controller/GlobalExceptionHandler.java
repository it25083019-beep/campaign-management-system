package com.example.campaign.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();

            String jpField = switch (field) {
                case "name" -> "氏名";
                case "email" -> "メール";
                case "phone" -> "電話番号";
                case "area" -> "地区";
                case "age" -> "年齢";
                default -> field;
            };

            String jpMessage = switch (message) {
                case "must not be blank" -> "必須項目です";
                case "must be a well-formed email address" -> "メール形式が正しくありません";
                case "must be greater than or equal to 0" -> "0以上で入力してください";
                default -> message;
            };

            errors.add(jpField + "：" + jpMessage);
        });

        Map<String, Object> result = new HashMap<>();
        result.put("message", "入力エラーがあります");
        result.put("errors", errors);

        return result;
    }
}