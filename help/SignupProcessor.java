package com.itr.obooks.service.signup;

import com.itr.obooks.dao.UserDao;
import com.itr.obooks.model.User;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class SignupProcessor implements Validator
{
    @Autowired
    private UserDao userDao;

    public boolean activateUser(Integer id)
    {
	return userDao.setActivated(id);
    }

    public void sendActivationLink(User user, String email)
    {
	BeanFactory beanfactory = new ClassPathXmlApplicationContext(
		"WEB-INF/spring/root-context.xml");
	Mail mail = (Mail) beanfactory.getBean("mail");

	mail.send("Obooks Activation Link",
		"http://localhost:8080/obooks/signup/" + user.getId(), email);
    }

    public User addUser(SignupForm signupForm)
    {
	User user = new User();
	user.setName(signupForm.getUsername());
	user.setEmail(signupForm.getEmail());
	String sha2password;
	try
	{
	    sha2password = SHAHashing.encrypt(signupForm.getPassword());
	    user.setPassword(sha2password);
	}
	catch(Exception e)
	{
	    return null;
	}
	userDao.create(user);
	return user;
    }

    public boolean noSuchUser(String login)
    {
	if(userDao.getByName(login) == null)
	    return true;
	else
	    return false;
    }

    public boolean supports(Class<?> clazz)
    {
	return SignupForm.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors)
    {
	SignupForm signupForm = (SignupForm) target;

	validateUserName(signupForm, errors);
	validatePasswords(signupForm, errors);
	validateEmail(signupForm, errors);
    }

    private void validateEmail(SignupForm signupForm, Errors errors)
    {
	if(!EmailValidator.getInstance().isValid(signupForm.getEmail()))
	{
	    errors.rejectValue("email", "email.notValid",
		    "Email address is not valid.");
	}
    }

    private void validatePasswords(SignupForm signupForm, Errors errors)
    {
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
		"password.empty", "Password must not be empty.");

	if(!(signupForm.getPassword()).equals(signupForm.getConfirmPassword()))
	{
	    errors.rejectValue("confirmPassword",
		    "confirmPassword.passwordDontMatch",
		    "Passwords don't match.");
	}
    }

    private void validateUserName(SignupForm signupForm, Errors errors)
    {
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
		"username.empty", "Username must not be empty.");

	String username = new String();
	try
	{
	    username = convertCode(signupForm.getUsername());
	}
	catch(Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if((username.length()) > 16)
	{
	    errors.rejectValue("username", "username.tooLong",
		    "Username must not more than 16 characters.");
	}

	if(!noSuchUser(username))
	    errors.rejectValue("username", "username.occupied",
		    "Username occupied");
    }

    private String convertCode(String buffer) throws Exception
    {
	String result = new String();
	for(int index = 0; index < buffer.length(); index++)
	{
	    if(index + 7 <= buffer.length()
		    && buffer.substring(index, index + 2).startsWith("&#"))
	    {
		int tempInteger = Integer.parseInt(buffer.substring(index + 2,
			index + 6));
		byte[] tempArray = new byte[2];
		tempArray[1] = (byte) tempInteger;
		tempArray[0] = (byte) (tempInteger >> 8);
		result += new String(tempArray, "UTF-16");
		index += 6;
	    }
	    else
	    {
		result += buffer.charAt(index);
	    }
	}
	return result;
    }
}
