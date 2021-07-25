package com.epam.esm;

import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.validator.impl.TagValidator;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.PagedModel;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    TagService tagService;

    private final static Long CORRECT_ID_VALUE = 1L;
    private final static Long NOT_EXIST_ID_VALUE = 999L;

    @BeforeEach
    void init(){
        tagService = new TagServiceImpl(tagRepository, new TagValidator());
    }

    @Nested
    class GettingByIdTests {

        @Test
        void correctGettingByIdShouldReturnTag() throws RepositoryException, ServiceException {
            Tag expected = new Tag(CORRECT_ID_VALUE,"name");
            when(tagRepository.getById(Mockito.anyLong())).thenReturn(expected);
            assertEquals(expected, tagService.getById(CORRECT_ID_VALUE));
            verify(tagRepository).getById(CORRECT_ID_VALUE);
        }

        @Test
        void gettingWithNotExistedIdShouldRaiseException() throws RepositoryException {
            when(tagRepository.getById(NOT_EXIST_ID_VALUE)).thenThrow(new DataNotExistRepositoryException());
            assertThrows(NotFoundServiceException.class,() -> tagService.getById(NOT_EXIST_ID_VALUE));
            verify(tagRepository).getById(NOT_EXIST_ID_VALUE);
        }

        @Test
        void gettingWithIncorrectIdShouldRaiseException() throws RepositoryException {
            assertAll(
                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.getById(-1L)),
                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.getById(null))
            );
            verify(tagRepository, never()).getById(any(Long.class));
        }
    }

    @Nested
    class DeletingTests {
        @Test
        void correctDeletingShouldNotRaiseException() throws ServiceException, RepositoryException {
            doNothing().when(tagRepository).deleteById(CORRECT_ID_VALUE);
            tagService.delete(CORRECT_ID_VALUE);
            verify(tagRepository).deleteById(CORRECT_ID_VALUE);
        }

        @Test
        void gettingWithNotExistedIdShouldRaiseException() throws RepositoryException {
            doThrow(new DataNotExistRepositoryException()).when(tagRepository).deleteById(NOT_EXIST_ID_VALUE);
            assertThrows(NotFoundServiceException.class, () -> tagService.delete(NOT_EXIST_ID_VALUE));
            verify(tagRepository).deleteById(NOT_EXIST_ID_VALUE);
        }

        @Test
        void gettingWithIncorrectIdShouldRaiseException() throws RepositoryException {
            assertAll(
                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.delete(-1L)),
                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.delete(null))
            );
            verify(tagRepository, never()).deleteById(any(Long.class));
        }
    }

//    @Nested
//    class GetTagsTests {
//
//        @Test
//        void correctGettingAllShouldReturnListOfTags() throws RepositoryException, ServiceException{
//            PagedDTO<Tag> expectedTagsDto = new PagedDTO<>(List.of(new Tag(1L,"name1"), new Tag(2L,"name2"),
//                    new Tag(3L,"name3")),new PagedModel.PageMetadata(1,1,1));
//            PagedDTO<Tag> emptyPagedDto = new PagedDTO<>();
//            doReturn(expectedTagsDto, emptyPagedDto).when(tagRepository).get(anyString(), anyInt(), anyInt());
//            assertEquals(expectedTagsDto, tagService.get("",1,1));
//            assertTrue(tagService.get("",1,1).isEmpty());
//            verify(tagRepository, times(2)).get(anyString(), anyInt(), anyInt());
//        }
//
//    }
//
//    @Nested
//    class AddingTests {
//        @Test
//        void correctAddingNewTagShouldNotRaiseException () throws RepositoryException, ServiceException {
//            Tag tagForAdding = new Tag(CORRECT_ID_VALUE,"name");
//            doNothing().when(tagRepository).add(tagForAdding);
//            tagService.add(tagForAdding);
//            verify(tagRepository).add(tagForAdding);
//        }
//
//        @Test
//        void addingExistedTagShouldRaiseException() throws RepositoryException {
//            doThrow(new DataAlreadyExistRepositoryException()).when(tagRepository).add(any(Tag.class));
//            assertThrows(AlreadyExistServiceException.class, () -> tagService.add(new Tag("existed_tag")));
//            verify(tagRepository).add(any(Tag.class));
//        }
//
//        @Test
//        void addingTagFailShouldThrowException() throws RepositoryException {
//            doThrow(new RepositoryException()).when(tagRepository).add(any(Tag.class));
//            assertThrows(ServiceException.class, () -> tagService.add(new Tag("some_tag")));
//            verify(tagRepository).add(any(Tag.class));
//        }
//
//        @Test
//        void addingIncorrectTagShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.add(null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.add(new Tag(null))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> tagService.add(new Tag("   ")))
//            );
//            verify(tagRepository, never()).add(any());
//        }
//    }
}
