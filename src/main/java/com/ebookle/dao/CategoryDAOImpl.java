package com.ebookle.dao;

import com.ebookle.entity.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class CategoryDAOImpl implements CategoryDAO{

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdate(Category category) {
        getSession().saveOrUpdate(category);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> findAll() {
        return getSession().createQuery("from " + Category.class.getName())
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean delete(int id) {
        Category ent = (Category) getSession().load(Category.class, id);
        if (ent != null) {
            getSession().delete(ent);
            return true;
        }
        return false;
    }

    @Override
    public Category findById (int id) {
        return (Category) getSession().get(Category.class, id);
    }

}
