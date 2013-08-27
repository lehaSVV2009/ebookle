package com.ebookle.dao;


import com.ebookle.entity.Book;
import com.ebookle.entity.Chapter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 24.08.13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public interface ChapterDAO {

    public void saveOrUpdate (Chapter book);

    public List<Chapter> findAll ();

    public boolean delete (int id);

    public Chapter findById (int id);

    public Chapter findByBookAndChapterNumber (Book book, Integer chapterNumber);

}
