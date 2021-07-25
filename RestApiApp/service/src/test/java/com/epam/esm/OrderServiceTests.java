package com.epam.esm;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//public class OrderServiceTests {
//
//
//    @Mock
//    OrderRepository orderRepository;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    GiftCertificateRepository certificateRepository;
//
//    OrderService service;
//
//    final Order orderSample = new Order(1L, LocalDateTime.now(), 1f);
//    final List<Order> orderListSample = List.of(new Order(1L, LocalDateTime.now(), 1f),
//            new Order(2L, LocalDateTime.now(), 2f),
//            new Order(3L, LocalDateTime.now(), 3f));
//    final PagedDTO<Order> orderPagedDtoSample = new PagedDTO<>(orderListSample, new PagedModel.PageMetadata(1,1,1));
//    final OrderCriteria criteriaSample = OrderCriteria.createCriteria(new HashMap<>());
//    final OrderDTO sampleDTO = new OrderDTO("someemail@mail.ru", 1L);
//    final String sampleEmail = "user@mail.com";
//    final long idSample = 1L;
//
//    @BeforeEach
//    void init(){
//        service = new OrderServiceImpl(orderRepository, userRepository, certificateRepository, new OrderCriteriaValidator(), new OrderValidator());
//    }
//
//    @Nested
//    class GetOrdersTests{
//
//        @Test
//        void CorrectGettingOrdersOfUserShouldReturnPagedDTO() throws RepositoryException, ServiceException {
//            PagedDTO<Order> expectedDto = orderPagedDtoSample;
//            PagedDTO<Order> emptyDto = new PagedDTO<>();
//            doReturn(expectedDto, emptyDto).when(orderRepository).getOrders(anyString(), any(OrderCriteria.class), anyInt(), anyInt());
//            assertEquals(expectedDto, service.getOrdersOfUser(sampleEmail, criteriaSample,3,1));
//            assertTrue(service.getOrdersOfUser(sampleEmail, criteriaSample,1,1).isEmpty());
//            verify(orderRepository, times(2)).getOrders(anyString(), any(OrderCriteria.class), anyInt(), anyInt());
//        }
//
//        @Test
//        void gettingWithIncorrectPageParamsShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.getOrdersOfUser(sampleEmail, criteriaSample,-1,2)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.getOrdersOfUser(sampleEmail, criteriaSample,2,-1)),
//                    () -> assertThrows(IncorrectDataServiceException.class, () -> service.getOrdersOfUser(sampleEmail, criteriaSample,0,0))
//            );
//            verify(orderRepository, never()).getOrders(anyString(), any(OrderCriteria.class), anyInt(), anyInt());
//        }
//
//        @Test
//        void requestForNonExistentPageShouldRaiseException() throws RepositoryException {
//            when(orderRepository.getOrders(anyString(), any(OrderCriteria.class), anyInt(), anyInt())).thenThrow(new IncorrectPageRepositoryException());
//            assertThrows(IncorrectPageServiceException.class, () -> service.getOrdersOfUser(sampleEmail, criteriaSample, 1, 999999));
//            verify(orderRepository).getOrders(sampleEmail, criteriaSample, 1, 999999);
//        }
//
//        @Test
//        void gettingWithIncorrectEmailShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrdersOfUser(null, criteriaSample, 1,1)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrdersOfUser("", criteriaSample, 1,1)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrdersOfUser("   ", criteriaSample, 1,1)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrdersOfUser("someString", criteriaSample, 1,1)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrdersOfUser("somnotemail111@ffffff.f.f.ff", criteriaSample, 1,1))
//            );
//            verify(orderRepository, never()).getOrders(anyString(), any(OrderCriteria.class), anyInt(), anyInt());
//        }
//    }
//
//    @Nested
//    class GettingByEmailTests {
//
//        @Test
//        void correctGettingByEmailShouldReturnOrder() throws RepositoryException, ServiceException {
//            Order expected = orderSample;
//            when(orderRepository.getOrder(sampleEmail, idSample)).thenReturn(expected);
//            assertEquals(expected, service.getOrder(sampleEmail, idSample));
//            verify(orderRepository).getOrder(anyString(), anyLong());
//        }
//
//        @Test
//        void gettingWithNotExistentEmailOrIdShouldRaiseException() throws RepositoryException {
//            String notExistentEmail = "some_email@some.com";
//            Long notExistentId = 99999L;
//            when(orderRepository.getOrder(notExistentEmail, idSample)).thenThrow(new DataNotExistRepositoryException());
//            when(orderRepository.getOrder(sampleEmail, notExistentId)).thenThrow(new DataNotExistRepositoryException());
//            assertThrows(NotFoundServiceException.class,() -> service.getOrder(notExistentEmail, idSample));
//            assertThrows(NotFoundServiceException.class,() -> service.getOrder(sampleEmail, notExistentId));
//
//            verify(orderRepository, times(2)).getOrder(anyString(), anyLong());
//        }
//
//        @Test
//        void gettingWithIncorrectEmailOrIdShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder(null, idSample)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder(sampleEmail, null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder(null, null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder("", idSample)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder("someString", idSample)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder("somnotemail111@ffffff.f.f.ff", idSample)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.getOrder(sampleEmail, -1L))
//            );
//            verify(orderRepository, never()).getOrder(anyString(), anyLong());
//        }
//    }
//
//    @Nested
//    class MakingOrderTests {
//
//        Order completeOrder;
//        User userFound = new User(sampleDTO.getEmail(), "someName", "someSurname");
//        GiftCertificate giftCertificateFound = new GiftCertificate(sampleDTO.getId(), "someString","someString",
//                1.1f, 5, LocalDateTime.now(), LocalDateTime.now());
//
//        @BeforeEach
//        void initMakingOrder(){
//            completeOrder = new Order();
//            completeOrder.setUser(userFound);
//            completeOrder.setGiftCertificate(giftCertificateFound);
//            completeOrder.setTimeOfPurchase(LocalDateTime.now());
//        }
//
//        @Test
//        void correctOrderingShouldReturnCompleteOrder () throws RepositoryException, ServiceException {
//            when(orderRepository.makeOrder(any(Order.class))).thenReturn(completeOrder);
//            when(userRepository.getByEmail(anyString())).thenReturn(userFound);
//            when(certificateRepository.getById(anyLong())).thenReturn(giftCertificateFound);
//            assertEquals(completeOrder, service.makeOrder(sampleDTO));
//            verify(orderRepository).makeOrder(any(Order.class));
//        }
//
//        @Test
//        void orderingWithNotExistentEmailOrIdShouldRaiseException() throws RepositoryException {
//            String notExistentEmail = "some_email@some.com";
//            Long notExistentId = 99999L;
//
//            lenient().when(userRepository.getByEmail(notExistentEmail)).thenThrow(DataNotExistRepositoryException.class);
//            lenient().when(userRepository.getByEmail(sampleEmail)).thenReturn(userFound);
//            lenient().when(certificateRepository.getById(notExistentId)).thenThrow(DataNotExistRepositoryException.class);
//            lenient().when(certificateRepository.getById(idSample)).thenReturn(giftCertificateFound);
//
//            assertAll(
//                    () ->  assertThrows(NotFoundServiceException.class,() -> service.makeOrder(new OrderDTO(notExistentEmail, idSample))),
//                    () ->  assertThrows(NotFoundServiceException.class,() -> service.makeOrder(new OrderDTO(sampleEmail, notExistentId))),
//                    () ->  assertThrows(NotFoundServiceException.class,() -> service.makeOrder(new OrderDTO(notExistentEmail, notExistentId)))
//            );
//
//            verify(orderRepository, never()).makeOrder(any(Order.class));
//        }
//
//
//        @Test
//        void addingIncorrectOrderShouldRaiseException() throws RepositoryException {
//            assertAll(
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.makeOrder(null)),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.makeOrder(new OrderDTO(null, idSample))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.makeOrder(new OrderDTO(sampleEmail, null))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.makeOrder(new OrderDTO("", idSample))),
//                    () -> assertThrows(IncorrectDataServiceException.class,() -> service.makeOrder(new OrderDTO("somenotemail", idSample)))
//            );
//
//            verify(orderRepository, never()).makeOrder(any(Order.class));
//            verify(userRepository, never()).getByEmail(anyString());
//            verify(certificateRepository, never()).getById(anyLong());
//        }
//    }

//}
