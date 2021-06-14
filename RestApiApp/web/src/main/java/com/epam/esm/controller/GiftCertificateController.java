package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.IncorrectDataServiceException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.exception.NotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.util.PatchUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in Certificate Controller";
    private final GiftCertificateService giftCertificateService;
    Logger logger = Logger.getLogger(GiftCertificateController.class);

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public List<GiftCertificate> getAllCertificates (@RequestParam(name = "tag", required = false) Optional<String> tagName){
        return giftCertificateService.get(tagName);
    }

    @GetMapping("/{id}")
    public GiftCertificate getCertificateById (@PathVariable("id") Integer id){

        GiftCertificate giftCertificate;
        try {
            giftCertificate = giftCertificateService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return giftCertificate;

    }

    @PostMapping()
    public GiftCertificate addNewCertificate(@RequestBody GiftCertificate giftCertificate){
        GiftCertificate certificateForResponse;
        try {
            certificateForResponse = giftCertificateService.add(giftCertificate);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return certificateForResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificate (@PathVariable("id") Integer id){
        try {
            giftCertificateService.delete(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public GiftCertificate updateCustomer(@PathVariable Integer id, @RequestBody JsonPatch patch) {
        GiftCertificate certificateForResponse;
        try {
            GiftCertificate current = giftCertificateService.getById(id);
            GiftCertificate modified = PatchUtil.applyPatch(patch, current, GiftCertificate.class);
            certificateForResponse = giftCertificateService.update(current, modified);
        } catch (NotFoundServiceException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (JsonPatchException | JsonProcessingException | ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return certificateForResponse;
    }
}
