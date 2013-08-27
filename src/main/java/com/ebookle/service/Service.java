package com.ebookle.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 25.08.13
 * Time: 5:51
 * To change this template use File | Settings | File Templates.
 */
@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Random random;

    public String createRandomKey () {
        return createUserKey(random.nextInt(20) + 20);
    }

    public String createUserKey (int passwordLength)
    {
        String valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        int validLength = valid.length();
        while (0 < passwordLength--) {
            result.append(
                    valid.charAt(
                            random.nextInt(validLength)));
        }
        return result.toString();
    }





}
