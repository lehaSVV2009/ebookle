package com.ebookle.service.validation;

import com.ebookle.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 29.08.13
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RegistrationValidator implements Validator {

    @Override
    public boolean supports (Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate (Object target, Errors errors) {

        User user = (User) target;
        validateLogin(user.getLogin(), errors);
        validatePassword(user.getPassword(), errors);
        validateEmail(user.getEmail(), errors);
    }

    private void validateLogin (String login, Errors errors) {

    }

    private void validatePassword (String password, Errors errors) {

    }

    private void validateEmail (String email, Errors errors) {

    }
}
