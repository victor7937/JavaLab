package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.IncorrectDataServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.exception.AlreadyExistServiceException;
import com.epam.esm.exception.NotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    Logger logger = Logger.getLogger(TagController.class);

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in Tag Controller";

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Get method for receiving list of all tags
     * @return List of tags in JSON
     */
    @GetMapping()
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    /**
     * Get method for receiving one tag by id if it exists
     * @param id - id of tag
     * @return tag found in JSON
     */
    @GetMapping("/{id}")
    public Tag getTagById (@PathVariable("id") Integer id){
        Tag tag;
        try {
            tag = tagService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return tag;
    }

    /**
     * Post method for adding a new tag
     * @param tag - tag for adding
     * @return tag that was added in JSON
     */
    @PostMapping()
    public ResponseEntity<Object> addNewTag(@RequestBody Tag tag) {
        try {
            tagService.add(tag);
        } catch (AlreadyExistServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.CONFLICT), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete method for deleting one tag by id if it exists
     * @param id - id of the tag
     * @return OK response if tag was deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag (@PathVariable("id") Integer id){
        try {
            tagService.delete(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private int generateStatusCode(HttpStatus status){
        return status.value() * 10 + 2;
    }

}
