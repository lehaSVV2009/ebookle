package com.ebookle.controller;

import com.ebookle.entity.Book;
import com.ebookle.entity.Chapter;
import com.ebookle.entity.User;
import com.ebookle.service.BookService;
import com.ebookle.service.ChapterService;
import com.ebookle.service.UserService;
import com.ebookle.util.UtilStrings;
import com.petebevin.markdown.MarkdownProcessor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 31.08.13
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class PdfConvertController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarkdownProcessor markdownProcessor;

    @Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping("/{userLogin}/editBook/{bookTitle}/{chapterNumber}/savePdf")
    public String savePdf (@PathVariable("userLogin") String userLogin,
                           @PathVariable("bookTitle") String bookTitle,
                           @PathVariable("chapterNumber") Integer chapterNumber,
                           ModelMap modelMap) {

        User author = userService.findByLogin(userLogin);
        Book book = bookService.findByTitleAndUserId(bookTitle, author);
        List<Chapter> chapters = chapterService.findAllByBook(book);
        String htmlBook = convertToHtmlBook(bookTitle, author.getName(), chapters);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("curl", htmlBook);
        map.add("storefile", "true");
        map.add("OutputFileName", "ebookle_book");
        map.add("PageNo", "true");
        RestTemplate restTemplate = new RestTemplate();
        String jsonStr = restTemplate.postForObject(UtilStrings.PDF_CONVERTER_SITE, map, String.class);
        JSONObject responseOfService = null;
        JSONParser parser = new JSONParser();
        try {
            responseOfService = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String pdfUrl = responseOfService.get("FileUrl").toString();
        modelMap.addAttribute("pdfUrl", pdfUrl);
        return "pdf_reference";
    }

    private String convertToHtmlBook (String bookTitle, String authorName, List<Chapter> chapters) {

        StringBuilder htmlBook = new StringBuilder();
        htmlBook.append("<html><body><h1>" + bookTitle + "<h1><br><br><h2>" + authorName + "</h2>");
        for (Chapter chapter : chapters) {
            htmlBook.append(
                    markdownProcessor.markdown(chapter.getText())
            );
        }
        htmlBook.append("</body></html>");
        return htmlBook.toString();
    }

}
