package com.epam.esm;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.validator.CertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {
    
    @Mock
    GiftCertificateRepository repository;
    
    GiftCertificateService service;

    final GiftCertificate certificateSample = new GiftCertificate("name","test GiftCertificate for adding",23.5f,2);
    final GiftCertificate certificateSample2 = new GiftCertificate(1,"name","test GiftCertificate for adding",23.5f,2, LocalDateTime.now(), LocalDateTime.now());
    final List<GiftCertificate> certificateListSample = List.of(
            new GiftCertificate("name1","test1",1.1f,1),
            new GiftCertificate("name2","test2",1.2f,2),
            new GiftCertificate("name3","test3",1.3f,3));
    final Integer idSample = 1;
    final Integer notExistingIdSample = 999;

    @BeforeEach
    void init(){
        service = new GiftCertificateServiceImpl(repository, new CertificateValidator());
    }


    @Nested
    class GettingByIdTests {
        
        @Test
        void correctGettingByIdShouldReturnCertificate(){

            try {
                GiftCertificate expected = certificateSample;
                certificateSample.setId(idSample);
                when(repository.getById(Mockito.anyInt())).thenReturn(expected);
                assertEquals(expected, service.getById(idSample));
                verify(repository).getById(idSample);
            } catch (RepositoryException | ServiceException e) {
                fail();
            }
        }

        @Test
        void gettingWithNotExistedIdShouldRaiseException(){

            try {
                when(repository.getById(notExistingIdSample)).thenThrow(new DataNotExistRepositoryException());
                assertThrows(NotFoundServiceException.class,() -> service.getById(notExistingIdSample));
                verify(repository).getById(notExistingIdSample);
            } catch (RepositoryException e) {
                fail();
            }
        }

        @Test
        void gettingWithIncorrectIdShouldRaiseException(){

            try {
                assertAll(
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.getById(-1)),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.getById(null))
                );
                verify(repository, never()).getById(any(Integer.class));
            } catch (RepositoryException e) {
                fail();
            }
        }
    }

    @Nested
    class DeletingTests {
        @Test
        void correctDeletingShouldNotRaiseException(){

            try {
                doNothing().when(repository).delete(idSample);
                service.delete(idSample);
                verify(repository).delete(idSample);
            } catch (RepositoryException | ServiceException e) {
                fail();
            }

        }

        @Test
        void gettingWithNotExistedIdShouldRaiseException(){

            try {
                doThrow(new DataNotExistRepositoryException()).when(repository).delete(notExistingIdSample);
                assertThrows(NotFoundServiceException.class, () -> service.delete(notExistingIdSample));
                verify(repository).delete(notExistingIdSample);
            } catch (RepositoryException e) {
                fail();
            }

        }

        @Test
        void gettingWithIncorrectIdShouldRaiseException(){

            try {
                assertAll(
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.delete(-1)),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.delete(null))
                );
                verify(repository, never()).delete(any(Integer.class));
            } catch (RepositoryException e) {
                fail();
            }

        }
    }

    @Nested
    class GetAllTests {

        @Test
        void correctGettingAllShouldReturnListOfGiftCertificates(){

            List<GiftCertificate> expected = certificateListSample;
            List<GiftCertificate> emptyList = Collections.emptyList();
            doReturn(expected, emptyList).when(repository).getAll();
            assertEquals(expected, service.get(Optional.empty()));
            assertEquals(emptyList ,service.get(Optional.empty()));
            verify(repository, times(2)).getAll();
        }

    }

    @Nested
    class AddingTests {
        @Test
        void correctAddingNewGiftCertificateShouldNotRaiseException (){
            try {
                GiftCertificate giftCertificateForReturn = certificateSample2;
                when(repository.add(any(GiftCertificate.class))).thenReturn(giftCertificateForReturn);
                assertEquals(giftCertificateForReturn, service.add(certificateSample));
                verify(repository).add(any(GiftCertificate.class));
            } catch (RepositoryException | ServiceException e) {
                fail();
            }
        }


        @Test
        void addingGiftCertificateFailShouldThrowException(){
            try {
                doThrow(new RepositoryException()).when(repository).add(any(GiftCertificate.class));
                assertThrows(ServiceException.class, () -> service.add(certificateSample));
                verify(repository).add(any(GiftCertificate.class));
            } catch (RepositoryException e) {
                fail();
            }
        }

        @Test
        void addingIncorrectGiftCertificateShouldRaiseException(){
            try {
                assertAll(
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.add(null)),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new GiftCertificate("","test1",1.1f,1))),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new GiftCertificate("name1","test1",null,1))),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new GiftCertificate("name1","test1",null,-1)))
                );
                GiftCertificate certificateForAdding = certificateSample;
                certificateForAdding.addTag(new Tag("name"));
                certificateForAdding.addTag(new Tag(null));
                assertThrows(IncorrectDataServiceException.class,() -> service.add(certificateForAdding));
                verify(repository, never()).add(any());
            } catch (RepositoryException e) {
                fail();
            }
        }
    }
}
