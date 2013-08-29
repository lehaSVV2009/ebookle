package com.ebookle.service;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 29.08.13
 * Time: 3:33
 * To change this template use File | Settings | File Templates.
 */
public interface RegistrationService {

    void register (String login, String password, String email, String name, String surname);
    boolean activateUser (String key);
}
