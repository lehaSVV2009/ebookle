package com.ebookle.service;

import com.ebookle.entity.User;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 29.08.13
 * Time: 3:33
 * To change this template use File | Settings | File Templates.
 */
public interface RegistrationService {

    void register (User user);
    boolean activateUser (String key);
}
