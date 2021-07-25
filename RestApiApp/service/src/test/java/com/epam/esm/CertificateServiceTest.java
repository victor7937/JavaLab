package com.epam.esm;

import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.validator.impl.CertificateCriteriaValidator;
import com.epam.esm.validator.impl.CertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

//    @Mock
//    GiftCertificateRepository repository;
//
//    @Mock
//    TagRepository tagRepository;
//
//    GiftCertificateService service;
//
//    final GiftCertificate certificateSample = new GiftCertificate("name","test GiftCertificate",23.5f,2);
//    final GiftCertificate certificateSample2 = new GiftCertificate(1L,"name","test GiftCertificate",23.5f,2, LocalDateTime.now(), LocalDateTime.now());
//    final List<GiftCertificate> certificateListSample = List.of(
//            new GiftCertificate("name1","test1",1.1f,1),
//            new GiftCertificate("name2","test2",1.2f,2),
//            new GiftCertificate("name3","test3",1.3f,3));
//    final CertificateDTO certificateDTOSample = new CertificateDTO("name","test GiftCertificate",23.5f,2, new HashSet<>());
//    final Long idSample = 1L;
//    final Long notExistingIdSample = 999L;
//
//    @BeforeEach
//    void init(){
//        service = new GiftCertificateServiceImpl(repository, tagRepository, new CertificateValidator(), new CertificateCriteriaValidator());
//    }
//
//    @Nested
//    class GettingByIdTests {
//
//        @Test
//        void correctGettingByIdShouldReturnCertificate() throws RepositoryException, ServiceException {
//            GiftCertificate expected = certificateSample;
//            certificateSample.setId(idSample);
//            when(repository.getById(Mockito.anyLong())).thenReturn(expected);
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
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getById(-1L)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getById(null))
//            );
//            verify(repository, never()).getById(any(Long.class));
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
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.delete(-1L)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.delete(null))
//            );
//            verify(repository, never()).delete(any(Long.class));
//        }
//    }
//
//    @Nested
//    class GetCertificatesTests {
//
//        @Test
//        void correctGettingByCriteriaShouldReturnPagedDto() throws RepositoryException, ServiceException{
//            PagedDTO<GiftCertificate> expectedDto = new PagedDTO<>(certificateListSample, new PagedModel.PageMetadata(1,1,1));
//            PagedDTO<GiftCertificate> emptyDto = new PagedDTO<>();
//            doReturn(expectedDto, emptyDto).when(repository).getByCriteria(any(CertificateCriteria.class), anyInt(), anyInt());
//            assertEquals(expectedDto, service.get(CertificateCriteria.createCriteria(new HashMap<>()),1,1));
//            assertTrue(service.get(CertificateCriteria.createCriteria(new HashMap<>()),1,1).isEmpty());
//            verify(repository, times(2)).getByCriteria(any(CertificateCriteria.class), anyInt(), anyInt());
//        }
//
//        @Test
//        void gettingWithIncorrectPageParamsShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.get(CertificateCriteria.createCriteria(new HashMap<>()),-1,2)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.get(CertificateCriteria.createCriteria(new HashMap<>()),2,-1)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.get(CertificateCriteria.createCriteria(new HashMap<>()),0,0))
//            );
//            verify(repository, never()).getByCriteria(any(CertificateCriteria.class), anyInt(), anyInt());
//        }
//
//    }
//
//    @Nested
//    class AddingTests {
//        @Test
//        void correctAddingNewGiftCertificateShouldNotRaiseException () throws RepositoryException, ServiceException {
//            GiftCertificate giftCertificateForReturn = certificateSample;
//            when(repository.add(any(CertificateDTO.class))).thenReturn(giftCertificateForReturn);
//            assertEquals(giftCertificateForReturn, service.add(certificateDTOSample));
//            verify(repository).add(any(CertificateDTO.class));
//        }
//
//
//        @Test
//        void addingGiftCertificateFailShouldThrowException() throws RepositoryException, ServiceException {
//            doThrow(new RepositoryException()).when(repository).add(any(CertificateDTO.class));
//            assertThrows(ServiceException.class, () -> service.add(certificateDTOSample));
//            verify(repository).add(any(CertificateDTO.class));
//        }
//
//        @Test
//        void addingIncorrectGiftCertificateShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add(null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new CertificateDTO("","test1",1.1f,1, new HashSet<>()))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new CertificateDTO("name1","test1",null,1, new HashSet<>()))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.add( new CertificateDTO("name1","test1",null,-1, new HashSet<>())))
//            );
//            CertificateDTO certificateForAdding = certificateDTOSample;
//            Set<Tag> tags = Set.of(new Tag("name"),new Tag(null));
//            certificateForAdding.setTags(tags);
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
//            when(repository.update(any(CertificateDTO.class), anyLong())).thenReturn(certificateSample2);
//            assertEquals(certificateSample2, service.update(certificateDTOSample, 1L));
//            verify(repository).update(any(CertificateDTO.class),anyLong());
//       }
//
//       @Test
//       void updatingWithIncorrectValuesShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update(null, 1L)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update( certificateDTOSample, null)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update( null, null)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.update( certificateDTOSample, -1L))
//            );
//           verify(repository, never()).update(any(), any());
//       }
//    }
}
