package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.message.ResponseExceptionMessage;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.NotFoundServiceException;
import com.epam.esm.service.exception.ServiceException;
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return tag;
    }

    @PostMapping()
    public Tag addNewTag(@RequestBody Tag tag) {
        tag.setId(5);
        System.out.println(tag);
        return tag;
    }



}
