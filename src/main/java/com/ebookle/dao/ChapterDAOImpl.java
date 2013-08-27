package com.ebookle.dao;

import com.ebookle.entity.Book;
import com.ebookle.entity.Chapter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 24.08.13
 * Time: 4:11
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ChapterDAOImpl implements ChapterDAO{

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(Chapter chapter) {
        getSession().saveOrUpdate(chapter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Chapter> findAll() {
        return getSession().createQuery("from " + Chapter.class.getName())
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean delete(int id) {
        Chapter ent = (Chapter) getSession().load(Chapter.class, id);
        if (ent != null) {
            getSession().delete(ent);
            return true;
        }
        return false;
    }

    @Override
    public Chapter findById (int id) {
        return (Chapter) getSession().get(Chapter.class, id);
    }

    @Override
    public Chapter findByBookAndChapterNumber (Book book, Integer chapterNumber) {
        return (Chapter) getSession().createCriteria(Chapter.class)
                .add(Restrictions.eq("book", book))
                .add(Restrictions.eq("chapterNumber", chapterNumber))
                .uniqueResult();
    }

}
