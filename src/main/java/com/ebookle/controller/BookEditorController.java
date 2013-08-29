package com.ebookle.controller;

import com.ebookle.entity.*;
import com.ebookle.service.*;
import com.ebookle.util.UtilStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 28.08.13
 * Time: 6:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class BookEditorController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ChapterService chapterService;


    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/bookCreation", method = RequestMethod.GET)
    public String goToBookCreation (@PathVariable("userLogin") String userLogin, ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("userLogin", userLogin);
        return "create_new_book";
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/createNewBook", method = RequestMethod.POST)
    public String createNewBook (Principal principal,
                                 @PathVariable("userLogin") String userLogin,
                                 @RequestParam("title") String title,
                                 @RequestParam("description") String description,
                                 @RequestParam("category") Integer categoryId,
                                 @RequestParam("bookTag") String bookTag,
                                 ModelMap modelMap) {

        //TODO: checkParams
        //TODO: checkBookName
        if (!principal.getName().equals(userLogin)) {
            modelMap.addAttribute("error", "You cannot enter site!");
            return "error";
        }
        User user = userService.findByLogin(principal.getName());
        Category category = categoryService.findById(categoryId);
        Book book = new Book(
                title,
                description,
                user,
                category
        );
        if (bookTag != null && !"".equals(bookTag)) {
            book = addTag(bookTag, book);
        }
        bookService.saveOrUpdate(book);
        return "redirect:/" + userLogin + "/editBook/" + title + "/1";
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/editBook/{bookTitle}/{chapterNumber}/addTag", method = RequestMethod.POST)
    public String addTag (Principal principal,
                          @PathVariable("chapterNumber") Integer chapterNumber,
                          @PathVariable("userLogin") String userLogin,
                          @PathVariable("bookTitle") String bookTitle,
                          @RequestParam("bookTag") String bookTag,
                          ModelMap modelMap) {
        if (principal == null
                || ! principal.getName().equals(userLogin)) {
            modelMap.addAttribute("error", "Страница недоступна!");
            return "error";
        }
        User user = userService.findByLogin(userLogin);
        Book book = bookService.findByTitleAndUserIdWithTags(bookTitle, user);
        book = addTag(bookTag, book);
        bookService.saveOrUpdate(book);
        return "redirect:/" + userLogin + "/editBook/" + bookTitle + "/" + chapterNumber;
    }

    private Book addTag(String bookTag, Book book) {
        Tag tag = new Tag();
        tag.setBookTag(bookTag);
        book.getTags().add(tag);
        return book;
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/editBook/{bookTitle}/{chapterNumber}", method = RequestMethod.GET)
    public String updateBook (Principal principal,
                              @PathVariable("chapterNumber") Integer chapterNumber,
                              @PathVariable("userLogin") String userLogin,
                              @PathVariable("bookTitle") String bookTitle,
                              ModelMap modelMap) {

        if (principal == null
                || ! principal.getName().equals(userLogin)) {
            modelMap.addAttribute("error", "Страница недоступна!");
            return "error";
        }
        User user = userService.findByLogin(principal.getName());
        Book book = bookService.findByTitleAndUserIdWithChapters(bookTitle, user);
        if (book.getChapters().size() == 0) {
            createChapter(book, 1);
            book = bookService.findByTitleAndUserIdWithChapters(bookTitle, user);
            chapterNumber = 1;
        }
        //  TODO: changeBookVersion
        //  TODO: change chapter version
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("userLogin", userLogin);
        modelMap.addAttribute("currentChapter", book.getChapters().get(chapterNumber - 1));
        modelMap.addAttribute("userAction", "edit");
        modelMap.addAttribute("person", "ownUser");
        modelMap.addAttribute("tags", bookService.findByTitleAndUserIdWithTags(bookTitle, user).getTags());
        return "edit_book";
    }

    //  Chapter Edition

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/editBook/{bookTitle}/createNewChapter", method = RequestMethod.GET)
    public String createNewChapter (Principal principal, @PathVariable("userLogin") String userLogin, @PathVariable("bookTitle") String bookTitle) {

        User user = userService.findByLogin(principal.getName());
        Book book = bookService.findByTitleAndUserIdWithChapters(bookTitle, user);
        createChapter(book, book.getChapters().size() + 1);
        //  TODO: changeBookVersion
        return ("redirect:/" + userLogin + "/editBook/" + bookTitle + "/" + (book.getChapters().size() + 1));
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/editBook/{bookTitle}/{chapterNumber}/save", method = RequestMethod.POST)
    public String saveChapter (@RequestParam("text") String text, Principal principal, @PathVariable("chapterNumber") Integer chapterNumber, @PathVariable("userLogin") String userLogin, @PathVariable("bookTitle") String bookTitle, ModelMap modelMap) {

        User user = userService.findByLogin(principal.getName());
        Book book = bookService.findByTitleAndUserId(bookTitle, user);
        Chapter chapter = chapterService.findByBookAndChapterNumber(book, chapterNumber);
        chapter.setText(text);
        chapterService.saveOrUpdate(chapter);
        return ("redirect:/" + userLogin + "/editBook/" + bookTitle + "/" + chapterNumber);
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/remove/{bookId}", method = RequestMethod.GET)
    public String deleteBook(Principal principal, @PathVariable("userLogin") String userLogin, @PathVariable("bookId")Integer bookId){
        if(principal != null) {
            if(principal.getName().equals(userLogin)) {
                bookService.delete(bookId);
            }
        }
        return "redirect:/";
    }

    private void createChapter (Book book, int number) {
        Chapter chapter = new Chapter(
                UtilStrings.STANDARD_CHAPTER_NAME + number,
                "Input text here",
                book,
                number
        );
        chapterService.saveOrUpdate(chapter);
    }

}
