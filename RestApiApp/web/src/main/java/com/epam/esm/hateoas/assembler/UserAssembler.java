package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserAssembler(ModelMapper modelMapper) {
        super(UserController.class, UserModel.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel model = instantiateModel(entity);
        modelMapper.map(entity, model);
        Link selfRel = linkTo(methodOn(UserController.class).getByEmail(entity.getEmail())).withSelfRel();
        Link ordersRel = linkTo(methodOn(UserController.class).getOrdersOfUser(entity.getEmail())).withRel("orders");
        model.add(selfRel, ordersRel);
        return model;
    }

    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
       CollectionModel<UserModel> models = super.toCollectionModel(entities);
       models.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
       return models;
    }

    public PagedModel<UserModel> toPagedModel(Collection<? extends User> entities, PagedModel.PageMetadata metadata) {
        return PagedModel.of(entities.stream().map(this::toModel).collect(Collectors.toList()), metadata);
    }
}
