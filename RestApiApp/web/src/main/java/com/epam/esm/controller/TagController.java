package com.epam.esm.controller;

import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.service.TagService;
import com.epam.esm.util.StatusCodeGenerator;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


/**
 * Controller for REST operations with tags
 * Makes get, get by id, add, delete and special calculating operations
 */
@RestController
@RequestMapping("/tags")
@Slf4j
public class TagController {

    private final TagService tagService;
   

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in Tag Controller";

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    /**
     * @param page current page
     * @param size size of the page
     * @param namePart part of the tags name for searching
     * @return page with tags found
     */
    @GetMapping(produces = { "application/prs.hal-forms+json" })
    public PagedModel<Tag> getTags(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                   @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                   @RequestParam (name = "part", required = false, defaultValue = "") String namePart) {
        PagedDTO<Tag> pagedDTO;
        try {
            pagedDTO = tagService.get(namePart, size, page);
        } catch (IncorrectPageServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e){
            log.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        if (pagedDTO.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return PagedModel.of(pagedDTO.getPage(), pagedDTO.getPageMetadata());
    }

    /**
     * Get method for receiving one tag by id if it exists
     * @param id - id of tag
     * @return tag found in JSON
     */
    @GetMapping("/{id}")
    public Tag getTagById (@PathVariable("id") Long id){
        Tag tag;
        try {
            tag = tagService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e){
            log.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
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
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.CONFLICT, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            log.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete method for deleting one tag by id if it exists
     * @param id - id of the tag
     * @return OK response if tag was deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag (@PathVariable("id") Long id){
        try {
            tagService.delete(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            log.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get method for finding the most widely used tag of a user with the highest cost of all orders
     * @return Tag that was found
     */
    @GetMapping("/most-used-tag")
    Tag getMostUsedTagOfValuableCustomer() {
        return tagService.getMostUsedTagOfValuableCustomer();
    }

}
