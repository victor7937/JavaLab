package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String SQL_GET_ALL = "SELECT * FROM tag";

    private static final String SQL_GET_BY_ID = "SELECT * FROM tag WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepositoryImpl (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag getById(int id) throws RepositoryException {
        return jdbcTemplate.query(SQL_GET_BY_ID, new BeanPropertyRowMapper<>(Tag.class), id)
                .stream()
                .findAny()
                .orElseThrow(DataNotExistRepositoryException::new);
    }


}
