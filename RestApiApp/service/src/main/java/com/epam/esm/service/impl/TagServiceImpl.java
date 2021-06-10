package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataAlreadyExistRepositoryException;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.AlreadyExistServiceException;
import com.epam.esm.service.exception.NotFoundServiceException;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private static final String NOT_EXIST_MSG = "Tag id with number %s doesn't exist";
    private static final String ALREADY_EXIST_MSG = "Tag with name %s already exists";

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }

    @Override
    public Tag getById(int id) throws ServiceException {
        Tag tag;
        try {
            tag = tagRepository.getById(id);
        } catch (DataNotExistRepositoryException e) {
           throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return tag;
    }

    @Override
    public void add(Tag tag) throws ServiceException {
        try {
            tagRepository.add(tag);
        } catch (DataAlreadyExistRepositoryException e) {
            throw new AlreadyExistServiceException(String.format(ALREADY_EXIST_MSG, tag.getName()), e);
        } catch (RepositoryException e) {
           throw new ServiceException(e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
           tagRepository.delete(id);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
