package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.IncorrectDataServiceException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.exception.NotFoundServiceException;
import com.epam.esm.exception.ServiceException;

import com.epam.esm.service.OrderService;
import com.epam.esm.util.PatchUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Controller for REST operations with gift certificates
 * Makes get, get by id, add, delete and update operations
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in Certificate Controller";

    private final GiftCertificateService giftCertificateService;

    private final OrderService orderService;

    Logger logger = Logger.getLogger(GiftCertificateController.class);

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, OrderService orderService) {
        this.giftCertificateService = giftCertificateService;
        this.orderService = orderService;
    }

    /**
     * Get method for receiving list of certificates by some criteria
     * @param tagName - tag for searching, param is optional
     * @param sortBy - field for sorting
     * @param sortOrder - sorting order(ASC or DESC)
     * @param namePart - part of certificate name for searching
     * @return List of certificates in JSON
     */
    @GetMapping()
    public List<GiftCertificate> getCertificates (@RequestParam(name = "tag", required = false) Optional<String> tagName,
                                                     @RequestParam(name = "sort", required = false) Optional<String> sortBy,
                                                     @RequestParam(name = "order", required = false) Optional<String> sortOrder,
                                                  @RequestParam(name = "part", required = false) Optional<String> namePart){
        return giftCertificateService.get(tagName, namePart, sortBy, sortOrder);
    }

    /**
     * Get method for receiving one certificate by id if it exists
     * @param id - id of certificate
     * @return certificate found in JSON
     */
    @GetMapping("/{id}")
    public GiftCertificate getCertificateById (@PathVariable("id") Long id){

        GiftCertificate giftCertificate;
        try {
            giftCertificate = giftCertificateService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return giftCertificate;

    }

    /**
     * Post method for adding a new gift certificate
     * @param giftCertificate - certificate for adding
     * @return certificate that was added in JSON
     */
    @PostMapping()
    public GiftCertificate addNewCertificate(@RequestBody CertificateDTO giftCertificate){
        GiftCertificate certificateForResponse;
        try {
            certificateForResponse = giftCertificateService.add(giftCertificate);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return certificateForResponse;
    }


    /**
     * Delete method for deleting one certificate by id if it exists
     * @param id - id of the certificate
     * @return OK response if certificate was deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificate (@PathVariable("id") Long id){
        try {
            giftCertificateService.delete(id);
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


    /**
     * Patch method for modifying existed gift certificate.
     * Accept patch commands in JSON format RFC6901.
     * @param id - id of certificate for modifying
     * @param patch - RFC6901 patch commands
     * @return modified certificate
     */
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public GiftCertificate updateCustomer(@PathVariable Long id, @RequestBody JsonPatch patch) {
        GiftCertificate certificateForResponse;
        try {
            ModelMapper modelMapper = new ModelMapper();
            CertificateDTO current = modelMapper.map(giftCertificateService.getById(id), CertificateDTO.class);
            CertificateDTO modified = PatchUtil.applyPatch(patch, current, CertificateDTO.class);
            certificateForResponse = giftCertificateService.update(modified, id);
        } catch (NotFoundServiceException e){
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (JsonPatchException | JsonProcessingException | ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return certificateForResponse;
    }

    @PostMapping("/buy")
    public Order buyCertificate(@RequestBody OrderDTO orderDTO){
        Order orderForResponse;
        try {
            orderForResponse = orderService.makeOrder(orderDTO);
        } catch (NotFoundServiceException e){
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return orderForResponse;
    }


    private int generateStatusCode(HttpStatus status){
        return status.value() * 10 + 1;
    }
}
