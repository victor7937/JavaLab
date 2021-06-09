package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.NotFoundServiceException;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public List<GiftCertificate> getAllCertificates (){
        return giftCertificateService.getAll();
    }

    @GetMapping("/{id}")
    public GiftCertificate getCertificateById (@PathVariable("id") int id){

        GiftCertificate giftCertificate;
        try {
            giftCertificate = giftCertificateService.getById(id);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return giftCertificate;

    }

    @PostMapping()
    public ResponseEntity<Object> addNewCertificate(@RequestBody GiftCertificate giftCertificate){
        try {
            giftCertificateService.add(giftCertificate);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
