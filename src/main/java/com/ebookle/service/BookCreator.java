package com.ebookle.service;

import com.ebookle.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 27.08.13
 * Time: 1:47
 * To change this template use File | Settings | File Templates.
 */
@org.springframework.stereotype.Service
public class BookCreator {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    public void createBook(Book book) {
        bookService.saveOrUpdate(book);
    }

    public CategoryService getCategoryService () {
        return categoryService;
    }

    public void setCategoryService (CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public BookService getBookService () {
        return bookService;
    }

    public void setBookService (BookService bookService) {
        this.bookService = bookService;
    }
}
