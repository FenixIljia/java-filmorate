package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRangeValidator implements ConstraintValidator<DateRange, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        minDate = LocalDate.parse(constraintAnnotation.min(), formatter);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isAfter(minDate);
    }
}