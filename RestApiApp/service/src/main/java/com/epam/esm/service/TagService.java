package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll();
    Tag getById(int id);
}
