package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.Order;
import com.epam.esm.hateoas.model.OrderModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public OrderAssembler(ModelMapper modelMapper) {
        super(UserController.class, OrderModel.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderModel toModel(Order entity) {
        OrderModel model = instantiateModel(entity);
        modelMapper.map(entity, model);
        Link self = linkTo(methodOn(UserController.class).getOrderOfUser(entity.getUser().getEmail(), entity.getId())).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getByEmail(entity.getUser().getEmail())).withRel("user");
        Link certificateLink = linkTo(methodOn(GiftCertificateController.class).getCertificateById(entity.getGiftCertificate()
                .getId())).withRel("certificate");
        model.add(self, userLink, certificateLink);

        return model;
    }

    public PagedModel<OrderModel> toPagedModel(Collection<? extends Order> entities, PagedModel.PageMetadata metadata) {
        return PagedModel.of(entities.stream().map(this::toModel).collect(Collectors.toList()), metadata);
    }
}
