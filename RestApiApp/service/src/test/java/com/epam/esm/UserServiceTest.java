package com.epam.esm;

import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.*;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.validator.impl.UserCriteriaValidator;
import com.epam.esm.validator.impl.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.PagedModel;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
//
//    @Mock
//    UserRepository repository;
//
//    UserService service;
//
//    final User userSample = new User("someuser@gmail.com","Sergey", "Smith");
//    final List<User> userListSample = List.of(new User("someuser@gmail.com","Sergey", "Smith"),
//            new User("someuser2@gmail.com","Sergey2", "Smith2"),
//            new User("someuser3@gmail.com","Sergey3", "Smith3"));
//    final PagedDTO<User> userPagedDtoSample = new PagedDTO<>(userListSample, new PagedModel.PageMetadata(1,1,1));
//    final UserCriteria criteriaSample = UserCriteria.createCriteria(new HashMap<>());
//
//    @BeforeEach
//    void init(){
//        service = new UserServiceImpl(repository, new UserCriteriaValidator(), new UserValidator());
//    }
//
//    @Nested
//    class GetUsersTests{
//
//        @Test
//        void CorrectGettingUsersShouldReturnPagedDTO() throws RepositoryException, ServiceException {
//            PagedDTO<User> expectedDto = userPagedDtoSample;
//            PagedDTO<User> emptyDto = new PagedDTO<>();
//            doReturn(expectedDto, emptyDto).when(repository).getByCriteria(any(UserCriteria.class), anyInt(), anyInt());
//            assertEquals(expectedDto, service.get(criteriaSample,3,1));
//            assertTrue(service.get(criteriaSample,1,1).isEmpty());
//            verify(repository, times(2)).getByCriteria(any(UserCriteria.class), anyInt(), anyInt());
//        }
//
//        @Test
//        void gettingWithIncorrectPageParamsShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.get(criteriaSample,-1,2)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.get(criteriaSample,2,-1)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.get(criteriaSample,0,0))
//            );
//            verify(repository, never()).getByCriteria(any(UserCriteria.class), anyInt(), anyInt());
//        }
//
//        @Test
//        void requestForNonExistentPageShouldRaiseException() throws RepositoryException {
//            when(repository.getByCriteria(any(UserCriteria.class), anyInt(), anyInt())).thenThrow(new IncorrectPageRepositoryException());
//            assertThrows(IncorrectPageServiceException.class, () -> service.get(criteriaSample,1,999999));
//            verify(repository).getByCriteria(criteriaSample, 1, 999999);
//        }
//    }
//
//    @Nested
//    class GettingByEmailTests {
//
//        @Test
//        void correctGettingByEmailShouldReturnUser() throws RepositoryException, ServiceException {
//            User expected = userSample;
//            when(repository.getByEmail(expected.getEmail())).thenReturn(expected);
//            assertEquals(expected, service.getByEmail(expected.getEmail()));
//            verify(repository).getByEmail(anyString());
//        }
//
//        @Test
//        void gettingWithNotExistentEmailShouldRaiseException() throws RepositoryException {
//            String notExistentEmail = "some_email@some.com";
//            when(repository.getByEmail(notExistentEmail)).thenThrow(new DataNotExistRepositoryException());
//            assertThrows(NotFoundServiceException.class,() -> service.getByEmail(notExistentEmail));
//            verify(repository).getByEmail(anyString());
//        }
//
//        @Test
//        void gettingWithIncorrectEmailShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getByEmail(null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getByEmail("")),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getByEmail("   ")),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getByEmail("someString")),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getByEmail("somnotemail111@ffffff.f.f.ff"))
//            );
//            verify(repository, never()).getByEmail(anyString());
//        }
// }
}
