package com.ebookle.controller;

import com.ebookle.entity.Book;
import com.ebookle.entity.User;
import com.ebookle.service.BookService;
import com.ebookle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 31.08.13
 * Time: 1:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class PreferController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping("/{userLogin}/editBook/{bookTitle}/{chapterNumber}/show/like")
    public String pressLike(@PathVariable("userLogin") String userLogin,
                            @PathVariable("bookTitle") String bookTitle,
                            @PathVariable("chapterNumber") Integer chapterNumber,
                            ModelMap modelMap) {
        Book book = findBookByTitleAndAuthorLogin(userLogin, bookTitle);
        if (book == null) {
            return sendErrorMessage(modelMap, "Page not found");
        }
        book.setRating(book.getRating() + 1);
        bookService.saveOrUpdate(book);
        return "redirect:/" + userLogin + "/editBook/" + bookTitle + "/" +  chapterNumber + "/show";
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping("/{userLogin}/editBook/{bookTitle}/{chapterNumber}/show/dislike")
    public String pressDislike(@PathVariable("userLogin") String userLogin,
                            @PathVariable("bookTitle") String bookTitle,
                            @PathVariable("chapterNumber") Integer chapterNumber,
                            ModelMap modelMap) {
        Book book = findBookByTitleAndAuthorLogin(userLogin, bookTitle);
        if (book == null) {
            return sendErrorMessage(modelMap, "Page not found");
        }
        book.setRating(book.getRating() - 1);
        bookService.saveOrUpdate(book);
        return "redirect:/" + userLogin + "/editBook/" + bookTitle + "/" +  chapterNumber + "/show";
    }

    private Book findBookByTitleAndAuthorLogin (String userLogin, String bookTitle) {
        User author = userService.findByLogin(userLogin);
        if(author == null) {
            return null;
        }
        return bookService.findByTitleAndUserId(bookTitle, author);
    }

    private String sendErrorMessage(ModelMap modelMap, String error) {
        modelMap.addAttribute("flashMessage", error);
        return "redirect:/";
    }

}
