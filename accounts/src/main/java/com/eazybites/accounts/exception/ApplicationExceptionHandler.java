package com.eazybites.accounts.exception;

import com.eazybites.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDto> handleCustomerExistException(Exception e, WebRequest request){
                ErrorResponseDto errorResponseDto = new ErrorResponseDto();
                errorResponseDto.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR);
                errorResponseDto.setErrorTime(LocalDateTime.now());
                errorResponseDto.setApiPath(request.getDescription(false));
                errorResponseDto.setErrorMessage(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        }

        @ExceptionHandler(CustomerAlreadyExistException.class)
        public ResponseEntity<ErrorResponseDto> handleCustomerExistException(CustomerAlreadyExistException e, WebRequest request){
                ErrorResponseDto errorResponseDto = new ErrorResponseDto();
                errorResponseDto.setErrorCode(HttpStatus.BAD_REQUEST);
                errorResponseDto.setErrorTime(LocalDateTime.now());
                errorResponseDto.setApiPath(request.getDescription(false));
                errorResponseDto.setErrorMessage(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleCustomerExistException(ResourceNotFoundException e, WebRequest request){
                ErrorResponseDto errorResponseDto = new ErrorResponseDto();
                errorResponseDto.setErrorCode(HttpStatus.NOT_FOUND);
                errorResponseDto.setErrorTime(LocalDateTime.now());
                errorResponseDto.setApiPath(request.getDescription(false));
                errorResponseDto.setErrorMessage(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
                Map<String, String> errorMap = new HashMap<>();

                List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();              // Get all the errors resulting from field validation
                allErrors.forEach((error)->{
                       String fieldName = ((FieldError) error).getField();        // method precendence type casting, ie method call is invoked first before variable type casting
                       String errorMessage=error.getDefaultMessage();
                       errorMap.put(fieldName,errorMessage);
                });
                return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);
        }


}
