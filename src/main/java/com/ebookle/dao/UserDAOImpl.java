package com.ebookle.dao;

import com.ebookle.entity.User;
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
public class UserDAOImpl implements UserDAO{

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(User user) {
        getSession().saveOrUpdate(user);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAll() {
        return getSession().createQuery("from " + User.class.getName())
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean delete(int id) {
        User ent = (User) getSession().load(User.class, id);
        if (ent != null) {
            getSession().delete(ent);
            return true;
        }
        return false;
    }

    @Override
    public User findById (int id) {
        return (User) getSession().get(User.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAllWithDetails () {
        return getSession().createCriteria(User.class)
                .setFetchMode("books", FetchMode.EAGER)
                .list();
    }

    @Override
    public List<User> findAllByRole (String role) {
        return getSession()
                .createCriteria(User.class)
                .add(Restrictions.eq("role", role))
                .add(Restrictions.eq("isActivated", new Boolean(true)))
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public User findByLogin (String login) {
        return (User) getSession()
                .createCriteria(User.class)
                .setFetchMode("books", FetchMode.EAGER)
                .add(Restrictions.eq("login", login))
                .uniqueResult();
    }


}
