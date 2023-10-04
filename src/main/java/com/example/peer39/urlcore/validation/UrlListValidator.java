package com.example.peer39.urlcore.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UrlListValidator implements ConstraintValidator<ValidUrlList, List<String>> {
    @Override
    public boolean isValid(List<String> urls, ConstraintValidatorContext context) {
        if (urls == null || urls.isEmpty()) {
            return false;
        }
        for (String url : urls) {
            if (url == null || url.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}