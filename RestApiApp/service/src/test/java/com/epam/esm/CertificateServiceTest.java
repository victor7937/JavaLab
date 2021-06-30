package com.epam.esm;

import com.epam.esm.entity.Criteria;
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
    
//    @Mock
//    GiftCertificateRepository repository;
//
//    GiftCertificateService service;
//
//    final GiftCertificate certificateSample = new GiftCertificate("name","test GiftCertificate for adding",23.5f,2);
//    final GiftCertificate certificateSample2 = new GiftCertificate(1,"name","test GiftCertificate for adding",23.5f,2, LocalDateTime.now(), LocalDateTime.now());
//    final List<GiftCertificate> certificateListSample = List.of(
//            new GiftCertificate("name1","test1",1.1f,1),
//            new GiftCertificate("name2","test2",1.2f,2),
//            new GiftCertificate("name3","test3",1.3f,3));
//    final Integer idSample = 1;
//    final Integer notExistingIdSample = 999;
//
//    @BeforeEach
//    void init(){
//        service = new GiftCertificateServiceImpl(repository, new CertificateValidator());
//    }
//
//    @Nested
//    class GettingByIdTests {
//
//        @Test
//        void correctGettingByIdShouldReturnCertificate() throws RepositoryException, ServiceException {
//            GiftCertificate expected = certificateSample;
//            certificateSample.setId(idSample);
//            when(repository.getById(Mockito.anyInt())).thenReturn(expected);
//            assertEquals(expected, service.getById(idSample));
//            verify(repository).getById(idSample);
//        }
//
//        @Test
//        void gettingWithNotExistedIdShouldRaiseException() throws RepositoryException {
//            when(repository.getById(notExistingIdSample)).thenThrow(new DataNotExistRepositoryException());
//            assertThrows(NotFoundServiceException.class,() -> service.getById(notExistingIdSample));
//            verify(repository).getById(notExistingIdSample);
//        }
//
//        @Test
//        void gettingWithIncorrectIdShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getById(-1)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getById(null))
//            );
//            verify(repository, never()).getById(any(Integer.class));
//        }
//    }
//
//    @Nested
//    class DeletingTests {
//
//        @Test
//        void correctDeletingShouldNotRaiseException() throws RepositoryException, ServiceException {
//            doNothing().when(repository).delete(idSample);
//            service.delete(idSample);
//            verify(repository).delete(idSample);
//        }
//
//        @Test
//        void gettingWithNotExistedIdShouldRaiseException() throws RepositoryException {
//            doThrow(new DataNotExistRepositoryException()).when(repository).delete(notExistingIdSample);
//            assertThrows(NotFoundServiceException.class, () -> service.delete(notExistingIdSample));
//            verify(repository).delete(notExistingIdSample);
//        }
//
//        @Test
//        void gettingWithIncorrectIdShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.delete(-1)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.delete(null))
//            );
//            verify(repository, never()).delete(any(Integer.class));
//        }
//    }
//
//    @Nested
//    class GetAllTests {
//
//        @Test
//        void correctGettingByCriteriaShouldReturnListOfGiftCertificates(){
//            List<GiftCertificate> expected = certificateListSample;
//            List<GiftCertificate> emptyList = Collections.emptyList();
//            doReturn(expected, emptyList).when(repository).getByCriteria(any(Criteria.class));
//            assertEquals(expected, service.get(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
//            assertEquals(emptyList ,service.get(Optional.of("someNotExistingTag"), Optional.empty(), Optional.empty(), Optional.empty()));
//            verify(repository, times(2)).getByCriteria(any(Criteria.class));
//        }
//
//    }
//
//    @Nested
//    class AddingTests {
//        @Test
//        void correctAddingNewGiftCertificateShouldNotRaiseException () throws RepositoryException, ServiceException {
//            GiftCertificate giftCertificateForReturn = certificateSample2;
//            when(repository.add(any(GiftCertificate.class))).thenReturn(giftCertificateForReturn);
//            assertEquals(giftCertificateForReturn, service.add(certificateSample));
//            verify(repository).add(any(GiftCertificate.class));
//        }
//
//
//        @Test
//        void addingGiftCertificateFailShouldThrowException() throws RepositoryException, ServiceException {
//            doThrow(new RepositoryException()).when(repository).add(any(GiftCertificate.class));
//            assertThrows(ServiceException.class, () -> service.add(certificateSample));
//            verify(repository).add(any(GiftCertificate.class));
//        }
//
//        @Test
//        void addingIncorrectGiftCertificateShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add(null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new GiftCertificate("","test1",1.1f,1))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new GiftCertificate("name1","test1",null,1))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new GiftCertificate("name1","test1",null,-1)))
//            );
//            GiftCertificate certificateForAdding = certificateSample;
//            certificateForAdding.addTag(new Tag("name"));
//            certificateForAdding.addTag(new Tag(null));
//            assertThrows(IncorrectDataServiceException.class,() -> service.add(certificateForAdding));
//            verify(repository, never()).add(any());
//        }
//    }
//
//    @Nested
//    class UpdatingTests{
//
//        @Test
//        void correctUpdatingShouldReturnModifiedGiftCertificate() throws RepositoryException, ServiceException {
//           when(repository.update(any(GiftCertificate.class), any(GiftCertificate.class))).thenReturn(certificateSample2);
//           assertEquals(certificateSample2, service.update(certificateSample, new GiftCertificate("name","test GiftCertificate for adding",23.5f,2)));
//           verify(repository).update(any(GiftCertificate.class),any(GiftCertificate.class));
//       }
//
//       @Test
//       void updatingWithIncorrectValuesShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update(null, certificateSample)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update( certificateSample, null)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update( null, null))
//            );
//           verify(repository, never()).update(any(), any());
//       }
//    }
}
