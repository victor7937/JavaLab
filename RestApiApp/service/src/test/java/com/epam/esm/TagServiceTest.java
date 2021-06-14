package com.epam.esm;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.validator.TagValidator;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    TagService tagService;

    private final static int CORRECT_ID_VALUE = 1;
    private final static int NOT_EXIST_ID_VALUE = 999;

    @BeforeEach
    void init(){
        tagService = new TagServiceImpl(tagRepository, new TagValidator());
    }

    @Nested
    class GettingByIdTests {

        @Test
        void correctGettingByIdShouldReturnTag(){

            try {
                Tag expected = new Tag(CORRECT_ID_VALUE,"name");
                when(tagRepository.getById(Mockito.anyInt())).thenReturn(expected);
                assertEquals(expected, tagService.getById(CORRECT_ID_VALUE));
                verify(tagRepository).getById(CORRECT_ID_VALUE);
            } catch (RepositoryException | ServiceException e) {
                fail();
            }

        }

        @Test
        void gettingWithNotExistedIdShouldRaiseException(){

            try {
                when(tagRepository.getById(NOT_EXIST_ID_VALUE)).thenThrow(new DataNotExistRepositoryException());
                assertThrows(NotFoundServiceException.class,() -> tagService.getById(NOT_EXIST_ID_VALUE));
                verify(tagRepository).getById(NOT_EXIST_ID_VALUE);
            } catch (RepositoryException e) {
                fail();
            }

        }

        @Test
        void gettingWithIncorrectIdShouldRaiseException(){

            try {
                assertAll(
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.getById(-1)),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.getById(null))
                );
                verify(tagRepository, never()).getById(any(Integer.class));
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
                doNothing().when(tagRepository).delete(CORRECT_ID_VALUE);
                tagService.delete(CORRECT_ID_VALUE);
                verify(tagRepository).delete(CORRECT_ID_VALUE);
            } catch (RepositoryException | ServiceException e) {
                fail();
            }

        }

        @Test
        void gettingWithNotExistedIdShouldRaiseException(){

            try {
                doThrow(new DataNotExistRepositoryException()).when(tagRepository).delete(NOT_EXIST_ID_VALUE);
                assertThrows(NotFoundServiceException.class, () -> tagService.delete(NOT_EXIST_ID_VALUE));
                verify(tagRepository).delete(NOT_EXIST_ID_VALUE);
            } catch (RepositoryException e) {
                fail();
            }

        }

        @Test
        void gettingWithIncorrectIdShouldRaiseException(){

            try {
                assertAll(
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.delete(-1)),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.delete(null))
                );
                verify(tagRepository, never()).delete(any(Integer.class));
            } catch (RepositoryException e) {
                fail();
            }

        }
    }

    @Nested
    class GetAllTests {

        @Test
        void correctGettingAllShouldReturnListOfTags(){

            List<Tag> expected = List.of(new Tag(1,"name1"), new Tag(2,"name2"), new Tag(3,"name3")) ;
            List<Tag> emptyList = Collections.emptyList();
            doReturn(expected, emptyList).when(tagRepository).getAll();
            assertEquals(expected, tagService.getAll());
            assertEquals(emptyList, tagService.getAll());
            verify(tagRepository, times(2)).getAll();
        }

    }

    @Nested
    class AddingTests {
        @Test
        void correctAddingNewTagShouldNotRaiseException (){
            try {
                Tag tagForAdding = new Tag(CORRECT_ID_VALUE,"name");
                doNothing().when(tagRepository).add(tagForAdding);
                tagService.add(tagForAdding);
                verify(tagRepository).add(tagForAdding);
            } catch (RepositoryException | ServiceException e) {
                fail();
            }

        }

        @Test
        void addingExistedTagShouldRaiseException(){
            try {
                doThrow(new DataAlreadyExistRepositoryException()).when(tagRepository).add(any(Tag.class));
                assertThrows(AlreadyExistServiceException.class, () -> tagService.add(new Tag("existed_tag")));
                verify(tagRepository).add(any(Tag.class));
            } catch (RepositoryException e) {
                fail();
            }
        }

        @Test
        void addingTagFailShouldThrowException(){
            try {
                doThrow(new RepositoryException()).when(tagRepository).add(any(Tag.class));
                assertThrows(ServiceException.class, () -> tagService.add(new Tag("some_tag")));
                verify(tagRepository).add(any(Tag.class));
            } catch (RepositoryException e) {
                fail();
            }
        }

        @Test
        void addingIncorrectTagShouldRaiseException(){
            try {
                assertAll(
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.add(null)),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.add(new Tag(null))),
                        () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.add(new Tag("   ")))
                );
                verify(tagRepository, never()).add(any());
            } catch (RepositoryException e) {
                fail();
            }
        }
    }
}
