package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> getAll();
    Tag getById(int id);
}
