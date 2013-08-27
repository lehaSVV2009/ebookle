package com.ebookle.dao;

import com.ebookle.entity.Book;
import org.hibernate.FetchMode;
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
public class BookDAOImpl implements BookDAO{

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(Book book) {
        getSession().saveOrUpdate(book);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Book> findAll() {
        return getSession().createQuery("from " + Book.class.getName())
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Book> findAllWithAuthors () {
        return getSession().createCriteria(Book.class).setFetchMode("user", FetchMode.EAGER)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean delete(int id) {
        Book ent = (Book) getSession().load(Book.class, id);
        if (ent != null) {
            getSession().delete(ent);
            return true;
        }
        return false;
    }

    @Override
    public Book findById (int id) {
        return (Book) getSession().get(Book.class, id);
    }

    @Override
    public Book findByIdWithAuthor (int id) {
        return (Book) getSession().createCriteria(Book.class).setFetchMode("user", FetchMode.EAGER)
                .add(Restrictions.idEq(id)).uniqueResult();
    }

}
