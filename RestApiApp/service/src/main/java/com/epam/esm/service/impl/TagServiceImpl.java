package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    private static final String INVALID_ID_MSG = "Tag id is invalid";
    private static final String INCORRECT_TAG_MSG = "Tag data is incorrect";
    private final TagRepository tagRepository;
    private final ServiceValidator<Tag, Long> validator;

    private static final String NOT_EXIST_MSG = "Tag id with number %s doesn't exist";
    private static final String ALREADY_EXIST_MSG = "Tag with name %s already exists";


    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ServiceValidator<Tag, Long> validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }

    @Override
    public Tag getById(Long id) throws ServiceException {
        if (!validator.isIdValid(id)){
            throw new IncorrectDataServiceException(INVALID_ID_MSG);
        }
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
        if (!validator.validate(tag)){
            throw new IncorrectDataServiceException(INCORRECT_TAG_MSG);
        }
        try {
            tagRepository.add(tag);
        } catch (DataAlreadyExistRepositoryException e) {
            throw new AlreadyExistServiceException(String.format(ALREADY_EXIST_MSG, tag.getName()), e);
        } catch (RepositoryException e) {
           throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        if (!validator.isIdValid(id)){
            throw new IncorrectDataServiceException(INVALID_ID_MSG);
        }
        try {
           tagRepository.delete(id);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
