package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface TagService {
    List<Tag> getAll();
    Tag getById(Integer id) throws ServiceException;
    void add(Tag tag) throws ServiceException;
    void delete(Integer id) throws ServiceException;
}
