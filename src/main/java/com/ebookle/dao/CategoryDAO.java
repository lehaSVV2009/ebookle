package com.ebookle.dao;

import com.ebookle.entity.Category;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 24.08.13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryDAO {

    public void saveOrUpdate (Category book);

    public List<Category> findAll ();

    public boolean delete (int id);

    public Category findById (int id);

}
