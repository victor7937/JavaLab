package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.hateoas.model.GiftCertificateModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler extends RepresentationModelAssemblerSupport<GiftCertificate, GiftCertificateModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public GiftCertificateAssembler(ModelMapper modelMapper) {
        super(GiftCertificateController.class, GiftCertificateModel.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public GiftCertificateModel toModel(GiftCertificate entity) {
        GiftCertificateModel model = instantiateModel(entity);
        modelMapper.map(entity, model);
        Link self = linkTo(methodOn(GiftCertificateController.class).getCertificateById(entity.getId())).withSelfRel();
        model.add(self);
        return model;
    }

    public PagedModel<GiftCertificateModel> toPagedModel(Collection<? extends GiftCertificate> entities, PagedModel.PageMetadata metadata) {
        return PagedModel.of(entities.stream().map(this::toModel).collect(Collectors.toList()), metadata);
    }
}
