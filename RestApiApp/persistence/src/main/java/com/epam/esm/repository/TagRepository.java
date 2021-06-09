package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface TagRepository {
    List<Tag> getAll();
    Tag getById(int id) throws RepositoryException;
    void add(Tag tag) throws RepositoryException;
}
