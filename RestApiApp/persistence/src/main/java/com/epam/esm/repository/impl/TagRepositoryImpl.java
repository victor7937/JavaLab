package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataAlreadyExistRepositoryException;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String SQL_GET_ALL = "SELECT * FROM tag";

    private static final String SQL_GET_BY_ID = "SELECT * FROM tag WHERE id = ?";

    private static final String SQL_ADD = "INSERT INTO tag(name) VALUES(?)";

    private static final String SQL_TAG_EXISTS = "SELECT EXISTS (SELECT 1 FROM tag WHERE tag.name = ?) AS tag_exists";

    private static final String CHECKING_FOR_TAG_FAIL_MSG = "Checking for tag existence fail";

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

    @Override
    public void add(Tag tag) throws RepositoryException {
        Boolean isTagExist = jdbcTemplate.queryForObject(SQL_TAG_EXISTS, new SingleColumnRowMapper<>(Boolean.class), tag.getName());
        if (isTagExist == null) {
            throw new RepositoryException(CHECKING_FOR_TAG_FAIL_MSG);
        }
        if (isTagExist) {
           throw new DataAlreadyExistRepositoryException();
        }
        jdbcTemplate.update(SQL_ADD, tag.getName());
    }


}
