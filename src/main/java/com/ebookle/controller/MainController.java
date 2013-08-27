package com.ebookle.controller;

import com.ebookle.entity.Book;
/*
import com.ebookle.entity.Category;
import com.ebookle.entity.Chapter;
*/
import com.ebookle.entity.Category;
import com.ebookle.entity.Chapter;
import com.ebookle.entity.User;
import com.ebookle.service.*;
import com.ebookle.util.UtilStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 24.08.13
 * Time: 1:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MainController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;
    /*@Autowired
    private ChapterService chapterService;
    @Autowired
    private CategoryService categoryService;    */
    @Autowired
    private Service service;

    @Autowired
    private BookCreator bookCreator;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ChapterService chapterService;

    //later: add rus internationalization
    //model.put(JRParameter.REPORT_RESOURCE_BUNDLE , resourceBundle);

    private User createRandomUser () {
        User user = new User(
                service.createRandomKey(),
                service.createRandomKey(),
                "lehaSVV2009test@gmail.com ",
                service.createRandomKey(),
                service.createRandomKey(),
                service.createRandomKey(),
                "ROLE_USER",
                true
        );
        return user;
    }

    private Book createRandomBook (User user) {
        return new Book(
                service.createRandomKey(),
                service.createRandomKey(),
                user,
                new Category()
        );
    }

    @RequestMapping("/")
    public String goHome (ModelMap modelMap, Principal principal, RedirectAttributes redirectAttributes) {

        try {
            List<Book> books = bookService.findAllWithAuthors();
            if (books == null) {
                return showFlashMessage("Bad database", redirectAttributes);
            }
            modelMap.addAttribute("books", books);

            if (principal != null) {
                String login = principal.getName();
                User user = userService.findByLogin(login);
                if (user == null) {
                    return showFlashMessage("Bad database", redirectAttributes);
                }

                modelMap.addAttribute("user", user);
                modelMap.addAttribute("userBooks", user.getBooks());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }

    @RequestMapping("/registration")
    public String goToRegistration (ModelMap modelMap) {
        return "registration";
    }

    @RequestMapping("/register")
    public String register (RedirectAttributes redirectAttributes,
                            @RequestParam("login") String login,
                            @RequestParam("password") String password,
                            @RequestParam("email") String email,
                            @RequestParam("name") String name,
                            @RequestParam("surname") String surname) {

        if (! checkParams(login, password, email, name, surname)) {
            redirectAttributes.addFlashAttribute("badInput", "Bad input!!!");
            return "redirect:/registration";
        }
        registrationService.register(login, password, email, name, surname);
        redirectAttributes.addFlashAttribute("flashMessage", "Data was send to your email!");
        return "redirect:/";
    }

    @RequestMapping(value = "/registration_success?", method = RequestMethod.GET)
    @ResponseBody
    public String registrationSuccess (ModelMap modelMap, @RequestParam("key") String key/*@RequestHeader("key") String userKey*/) {

        //TODO: registration of User(get headers)
        modelMap.addAttribute("registrationSuccess", key);
        /*if (registrationService.activateUser("someKey")) {
            modelMap.addAttribute("registrationSuccess", "Registration was successful!");
            return "registration_success";
        } */
        return "registration_failed";
    }


    private boolean checkParams (String login, String password, String email, String name, String surname) {
        if ("".equals(login.trim())
                || "".equals(password.trim())
                || "".equals(email.trim())
                || "".equals(name.trim())
                || "".equals(surname.trim())) {
            return false;
        }
        if (! checkLogin(login)) {
            return false;
        }
        return true;
    }

    private boolean checkLogin (String login) {
        //TODO: check login in database
        return true;
    }

    @RequestMapping(value = "/welcome")
    public String welcomeUser (ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("userLogin", principal.getName());
        return "redirect:/";
    }

    @RequestMapping(value = "/login_fail")
    public String loginFail (RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("flashMessage", "Login was failed!");
        return "redirect:/";
    }

    @RequestMapping(value = "/logout")
    public String logout (RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("flashMessage", "You are log out");
        return "redirect:/";
    }

    @RequestMapping(value = "/{userLogin}/showBook/{id}", method = RequestMethod.GET)
    public String showBooks (@PathVariable("id") Integer id, ModelMap modelMap) {

        Book book = bookService.findByIdWithAuthor(id);
        modelMap.addAttribute("book", book);
        return "show_book";
    }

    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String goToAdminPage (ModelMap modelMap) {

        List<User> users = adminService.getUsersHasRoleUser();
        modelMap.addAttribute("users", users);
        return "admin";
    }

    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/removeUser/{id}", method = RequestMethod.POST)
    public String removeUser (@PathVariable("id") Integer userId, ModelMap modelMap) {

        adminService.removeUser(userId);
        return "redirect:/admin";
    }

    //  Book creation

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/bookCreation", method = RequestMethod.GET)
    public String goToBookCreation (@PathVariable("userLogin") String userLogin, ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("userLogin", userLogin);
        return "create_new_book";
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/createNewBook", method = RequestMethod.POST)
    public String createNewBook (@PathVariable("userLogin") String userLogin, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam("category") Integer categoryId, ModelMap modelMap) {

        //TODO: checkParams
        //TODO: checkBookName
        User user = userService.findByLogin(userLogin);
        Category category = categoryService.findById(categoryId);
        Book book = new Book(
                title,
                description,
                user,
                category
        );
        bookService.saveOrUpdate(book);
        return "redirect:/" + userLogin + "/editBook/" + title + "/1";
    }

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/{userLogin}/editBook/{bookTitle}/{chapterNumber}", method = RequestMethod.GET)
    public String updateBook (Principal principal, @PathVariable("chapterNumber") Integer chapterNumber, @PathVariable("userLogin") String userLogin, @PathVariable("bookTitle") String bookTitle, ModelMap modelMap) {

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
        return "edit_book";
    }

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


    private void createChapter (Book book, int number) {
        Chapter chapter = new Chapter(
                UtilStrings.STANDARD_CHAPTER_NAME + number,
                "Input text here",
                book,
                number
        );
        chapterService.saveOrUpdate(chapter);
    }


    //  Book edition



    //  Show message

    private String showFlashMessage (String flashMessage, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("flashMessage", flashMessage);
        return "home";
    }

/*

    @RequestMapping("/addUser")
    public String addUser(RedirectAttributes redirectAttributes) {

        try {
            User user = new User();
            user.setLogin("login4");
            user.setPassword("pass4");
            user.setEmail("l4@gmail.com");
            user.setActivated(false);
            user.setName("Аможеталексей");
            user.setSurname("Аможеторока");
            userService.saveOrUpdate(user);
            redirectAttributes.addFlashAttribute("message", "User added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
*/
}
