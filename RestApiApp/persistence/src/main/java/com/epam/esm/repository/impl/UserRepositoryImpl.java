package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    private static final String JPQL_GET_ALL = "SELECT u FROM User u";

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<User> getAll() {
        return entityManager.createQuery(JPQL_GET_ALL, User.class).getResultList();
    }

    @Override
    @Transactional
    public User getByEmail(String email) throws RepositoryException {
        return Optional.ofNullable(entityManager.find(User.class, email)).orElseThrow(DataNotExistRepositoryException::new);
    }

}
