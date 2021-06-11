package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.message.ResponseExceptionMessage;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.AlreadyExistServiceException;
import com.epam.esm.service.exception.NotFoundServiceException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.util.PatchUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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

    @GetMapping()
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public Tag getTagById (@PathVariable("id") int id){
        Tag tag;
        try {
            tag = tagService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return tag;
    }

    @PostMapping()
    public ResponseEntity<Object> addNewTag(@RequestBody Tag tag) {
        try {
            tagService.add(tag);
        } catch (AlreadyExistServiceException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag (@PathVariable("id") int id){
        try {
            tagService.delete(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
