package com.ebookle.controller;

import com.ebookle.entity.Book;
/*
import com.ebookle.entity.Category;
import com.ebookle.entity.Chapter;
*/
import com.ebookle.entity.User;
import com.ebookle.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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

    /*@Autowired
    private SimpleMailMessage preConfiguredMessage;

    @Autowired
    private MailSender mailSender;
    */

    //later: add rus internationalization
    //model.put(JRParameter.REPORT_RESOURCE_BUNDLE , resourceBundle);


    private User createRandomUser() {
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

    private Book createRandomBook(User user) {
        return new Book(
                service.createRandomKey(),
                service.createRandomKey(),
                user
        );
    }

    @RequestMapping("/")
    public String goHome(ModelMap modelMap, Principal principal) {

        try {
            List<Book> books = bookService.findAllWithAuthors();
            modelMap.addAttribute("books", books);

            if (principal != null) {
                String login = principal.getName();
                User user = userService.findByLogin(login);
                modelMap.addAttribute("user", user);
                modelMap.addAttribute("userBooks", user.getBooks());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }

    @RequestMapping("/registration")
    public String goToRegistration(ModelMap modelMap) {
        return "registration";
    }

    @RequestMapping("/register")
    public String register(RedirectAttributes redirectAttributes,
                           @RequestParam("login") String login,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("name") String name,
                           @RequestParam("surname") String surname) {

        if (!checkParams(login, password, email, name, surname)) {
            redirectAttributes.addFlashAttribute("badInput", "Bad input!!!");
            return "redirect:/registration";
        }
        registrationService.register(login, password, email, name, surname);
        redirectAttributes.addFlashAttribute("flashMessage", "Data was send to your email!");
        return "redirect:/";
    }

    @RequestMapping(value = "/registration_success", method = RequestMethod.GET)
    @ResponseBody
    public String registrationSuccess(ModelMap modelMap/*@RequestHeader("key") String userKey*/) {

        //  later: registration of User(get headers)
        if (registrationService.activateUser("someKey")) {
            modelMap.addAttribute("registrationSuccess", "Registration was successful!");
            return "registration_success";
        }
        return "registration_failed";
    }


    private boolean checkParams(String login, String password, String email, String name, String surname) {
        if ("".equals(login.trim())
                || "".equals(password.trim())
                || "".equals(email.trim())
                || "".equals(name.trim())
                || "".equals(surname.trim())) {
            return false;
        }
        if(!checkLogin(login)) {
            return false;
        }
        return true;
    }

    private boolean checkLogin(String login) {
        //later: check login in database
        return true;
    }

    @RequestMapping(value = "/welcome")
    public String welcomeUser(ModelMap modelMap) {
        return "redirect:/";
    }

    @RequestMapping(value = "/login_fail")
    public String loginFail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("flashMessage", "Login was failed!");
        return "redirect:/";
    }

    @RequestMapping(value = "/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("flashMessage", "You are log out");
        return "redirect:/";
    }

    @RequestMapping(value = "/showBook/{id}", method = RequestMethod.GET)
    public String showBooks(@PathVariable("id") Integer id, ModelMap modelMap) {

        Book book = bookService.findByIdWithAuthor(id);
        modelMap.addAttribute("book", book);
        return "show_book";
    }

    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String goToAdminPage(ModelMap modelMap) {

        List<User> users = adminService.getUsersHasRoleUser();
        modelMap.addAttribute("users", users);
        return "admin";
    }

    @Secured("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/removeUser/{id}", method = RequestMethod.POST)
    public String removeUser(@PathVariable("id") Integer userId, ModelMap modelMap) {

        adminService.removeUser(userId);
        return "redirect:/admin";
    }

    //  Book creation

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/goToBookCreation", method = RequestMethod.GET)
    public String goToBookCreation() {
        return "create_new_book";
    }


    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/createNewBook", method = RequestMethod.POST)
    public String createNewBook(@RequestParam("category") String categoryName, ModelMap modelMap) {

        modelMap.addAttribute("category", categoryName);
        return "edit_book";
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
