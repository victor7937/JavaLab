package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.IncorrectDataServiceException;
import com.epam.esm.exception.IncorrectPageServiceException;
import com.epam.esm.hateoas.assembler.GiftCertificateAssembler;
import com.epam.esm.hateoas.assembler.OrderAssembler;
import com.epam.esm.hateoas.model.GiftCertificateModel;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.exception.NotFoundServiceException;
import com.epam.esm.exception.ServiceException;

import com.epam.esm.service.OrderService;
import com.epam.esm.util.PatchUtil;
import com.epam.esm.util.StatusCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller for REST operations with gift certificates
 * Makes get, get by id, add, delete, update and buy operations
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in Certificate Controller";

    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateAssembler certificateAssembler;

    private final OrderAssembler orderAssembler;

    private final OrderService orderService;

    Logger logger = Logger.getLogger(GiftCertificateController.class);

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     GiftCertificateAssembler certificateAssembler,
                                     OrderAssembler orderAssembler, OrderService orderService) {
        this.giftCertificateService = giftCertificateService;
        this.certificateAssembler = certificateAssembler;
        this.orderAssembler = orderAssembler;
        this.orderService = orderService;
    }

    /**
     * Get method for receiving paged list of certificates by some criteria
     * @return List of certificates in JSON
     */
    @GetMapping(produces = { "application/prs.hal-forms+json" })
    public PagedModel<GiftCertificateModel> getCertificates ( @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                              @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                              @RequestParam Map<String, String> criteriaParams) {
        PagedDTO<GiftCertificate> pagedDTO;
        try {
             pagedDTO = giftCertificateService.get(CertificateCriteria.createCriteria(criteriaParams), size, page);
        } catch (IncorrectPageServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        if (pagedDTO.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return certificateAssembler.toPagedModel(pagedDTO.getPage(), pagedDTO.getPageMetadata());
    }

    /**
     * Get method for receiving one certificate by id if it exists
     * @param id - id of certificate
     * @return certificate found in JSON
     */
    @GetMapping(value = "/{id}", produces = { "application/prs.hal-forms+json" })
    public GiftCertificateModel getCertificateById (@PathVariable("id") Long id){
        GiftCertificate giftCertificate;
        try {
            giftCertificate = giftCertificateService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        GiftCertificateModel certificateModel = certificateAssembler.toModel(giftCertificate);
        addAffordances(certificateModel);
        return certificateModel;
    }

    /**
     * Post method for adding a new gift certificate
     * @param giftCertificate - certificate for adding
     * @return certificate that was added in JSON
     */
    @PostMapping( produces = { "application/prs.hal-forms+json" })
    public GiftCertificateModel addNewCertificate(@RequestBody CertificateDTO giftCertificate){
        GiftCertificate certificateForResponse;
        try {
            certificateForResponse = giftCertificateService.add(giftCertificate);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return certificateAssembler.toModel(certificateForResponse);
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
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
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
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json",  produces = { "application/prs.hal-forms+json" })
    public GiftCertificateModel updateCustomer(@PathVariable Long id, @RequestBody JsonPatch patch) {
        GiftCertificate certificateForResponse;
        try {
            ModelMapper modelMapper = new ModelMapper();
            CertificateDTO current = modelMapper.map(giftCertificateService.getById(id), CertificateDTO.class);
            CertificateDTO modified = PatchUtil.applyPatch(patch, current, CertificateDTO.class);
            certificateForResponse = giftCertificateService.update(modified, id);
        } catch (NotFoundServiceException e){
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (JsonPatchException | JsonProcessingException | ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return certificateAssembler.toModel(certificateForResponse);
    }

    /**
     * Post method for buying gift certificate by user
     * @param orderDTO contains users email and certificates id
     * @return Order with all data about purchase
     */
    @PostMapping(value = "/buy", produces = { "application/prs.hal-forms+json" })
    public OrderModel buyCertificate(@RequestBody OrderDTO orderDTO){
        Order orderForResponse;
        try {
            orderForResponse = orderService.makeOrder(orderDTO);
        } catch (NotFoundServiceException e){
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return orderAssembler.toModel(orderForResponse);
    }

    private void addAffordances(GiftCertificateModel model){
        model.mapLink(IanaLinkRelations.SELF, l -> l
                .andAffordance(afford(methodOn(GiftCertificateController.class).addNewCertificate(null)))
                .andAffordance(afford(methodOn(GiftCertificateController.class).updateCustomer(model.getId(),null)))
                .andAffordance(afford(methodOn(GiftCertificateController.class).deleteCertificate(model.getId())))
                .andAffordance(afford(methodOn(GiftCertificateController.class).buyCertificate(null))));
    }
    
}
