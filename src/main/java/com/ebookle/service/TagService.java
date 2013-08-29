package com.ebookle.service;

import com.ebookle.dao.TagDAO;
import com.ebookle.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 25.08.13
 * Time: 5:51
 * To change this template use File | Settings | File Templates.
 */
@org.springframework.stereotype.Service
public class TagService {

    @Autowired
    private TagDAO tagDAO;

    @Transactional
    public void saveOrUpdate (Tag tag) {
        tagDAO.saveOrUpdate(tag);
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll () {
        return tagDAO.findAll();
    }

    @Transactional
    public boolean delete (int id) {
        return tagDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public Tag findById (int id) {
        return tagDAO.findById(id);
    }
}
