package com.ebookle.dao;

import com.ebookle.entity.Book;
import com.ebookle.entity.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 24.08.13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public interface BookDAO {

    public void saveOrUpdate (Book book);

    public List<Book> findAll ();

    public List<Book> findAllWithAuthors ();

    public boolean delete (int id);

    public Book findById (int id);

    public Book findByIdWithAuthor (int id);

    public Book findByTitleAndUserId (String title, User user);
}