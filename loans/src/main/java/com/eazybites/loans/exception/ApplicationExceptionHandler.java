package com.eazybites.loans.exception;

import com.eazybites.loans.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionFromApplication(Exception e, WebRequest request){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setApi(request.getDescription(false));
        errorResponseDto.setErrorMessage(e.getLocalizedMessage());
        errorResponseDto.setErrorTime(LocalDateTime.now());
        errorResponseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setApi(request.getDescription(false));
        errorResponseDto.setErrorMessage(e.getLocalizedMessage());
        errorResponseDto.setErrorTime(LocalDateTime.now());
        errorResponseDto.setStatusCode(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleLoanAlreadyExistException(LoanAlreadyExistException e, WebRequest request){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setApi(request.getDescription(false));
        errorResponseDto.setErrorMessage(e.getLocalizedMessage());
        errorResponseDto.setErrorTime(LocalDateTime.now());
        errorResponseDto.setStatusCode(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        errorResponseDto.setApi(request.getDescription(false));
        errorResponseDto.setErrorTime(LocalDateTime.now());
        errorResponseDto.setStatusCode(HttpStatus.BAD_REQUEST);
        StringJoiner stringJoiner = new StringJoiner(",");
        allErrors.forEach((error)->{
            String field = ((FieldError) error).getField();
            field=field+" produces this error :"+error.getDefaultMessage();
            stringJoiner.add(field);
        });
        errorResponseDto.setErrorMessage(stringJoiner.toString());
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

}
