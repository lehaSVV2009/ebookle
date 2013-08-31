package com.ebookle.controller;

import com.sun.jndi.toolkit.url.UrlUtil;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 31.08.13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HelpController {

    public String decodeWithUtf (String text) {
        try {
            text = UrlUtil.decode(text, "UTF-8");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String encodeToUtf (String text) {
        try {
            text = UrlUtil.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

}
