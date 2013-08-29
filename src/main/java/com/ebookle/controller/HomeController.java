package com.ebookle.controller;

import com.ebookle.entity.Book;
import com.ebookle.entity.User;
import com.ebookle.service.BookService;
import com.ebookle.service.UserService;
import com.ebookle.util.UtilStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 29.08.13
 * Time: 6:37
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;



    @RequestMapping(value = "/home/{searchType}")
    public String showMostPopularBooks(ModelMap modelMap,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes,
                                       @PathVariable("searchType") String searchType) {
        try {

            //TODO: check searchType on exist

            List<Book> books = new ArrayList<Book>();
            if ("mostPopular".equals(searchType)) {
                books = bookService.findMostPopularWithAuthors();
            }
            if ("recent".equals(searchType)) {
                books = bookService.findRecentWithAuthors();
            }
            if (books == null) {
                return showFlashMessage("Bad database", redirectAttributes);
            }
            modelMap.addAttribute("books", books);
            if (principal == null) {
                modelMap.addAttribute("person", UtilStrings.GUEST_PERSON);
                return "home";
            }
            String login = principal.getName();
            User user = userService.findByLogin(login);
            if (user == null) {
                return showFlashMessage("Bad database", redirectAttributes);
            }
            modelMap.addAttribute("person", UtilStrings.USER_PERSON);
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("userBooks", user.getBooks());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }

    @RequestMapping(value = "/")
    public String goHome (ModelMap modelMap,
                          Principal principal,
                          RedirectAttributes redirectAttributes) {

        return "redirect:/home/mostPopular";
    }

    @RequestMapping("/home")
    public String goHome () {
        return "redirect:/";
    }


    private String showFlashMessage (String flashMessage, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("flashMessage", flashMessage);
        return "home";
    }

}