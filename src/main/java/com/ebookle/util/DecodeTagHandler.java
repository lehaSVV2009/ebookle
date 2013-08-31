package com.ebookle.util;

import com.sun.jndi.toolkit.url.UrlUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 01.09.13
 * Time: 0:35
 * To change this template use File | Settings | File Templates.
 */
public class DecodeTagHandler extends TagSupport {

    private String text;

    @Override
    public int doStartTag () throws JspException {

        JspWriter out = pageContext.getOut();
        try {
            text = encodeToUtf(text);
            out.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public String getText () {
        return text;
    }

    public void setText (String text) {
        this.text = text;
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
