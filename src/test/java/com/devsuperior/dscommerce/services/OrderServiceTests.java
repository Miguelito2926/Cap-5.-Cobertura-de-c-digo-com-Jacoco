package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.OrderFactory;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private Long existingOrderId, nonExistsOrderId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;
    private Long extingProductId;
    private Long nonExtingProductId;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistsOrderId = 2L;
        extingProductId = 1L;
        nonExtingProductId = 2L;
        admin = UserFactory.createCustomAdmintUser(1L, "Ed");
        client = UserFactory.createCustomClientUser(2L, "Miguel");
        product = ProductFactory.createProduct();
        order = OrderFactory.createOrder(client);
        order.setId(existingOrderId);
        orderDTO = new OrderDTO(order);

        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistsOrderId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getReferenceById(extingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExtingProductId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(orderRepository.save(any())).thenReturn(order);

        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {

        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        OrderDTO result = orderService.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() {

        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        OrderDTO result = orderService.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldThrowsForbbidenExceptionWhenIdExistsAndOtherClientLogged() {

        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());
        Assertions.assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = orderService.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldThrowsResourceNotfoundWhenDoesNotIdExists() {

        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = orderService.findById(nonExistsOrderId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOAdminLogged() {
        Mockito.when(userService.authenticated()).thenReturn(admin);
        OrderDTO result  = orderService.insert(orderDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingOrderId, result.getId());
    }

    @Test
    public void insertShouldReturnOrderDTOClientLogged() {
        Mockito.when(userService.authenticated()).thenReturn(client);
        OrderDTO result  = orderService.insert(orderDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingOrderId, result.getId());
    }

    @Test
    public void insertShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {

        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            OrderDTO result = orderService.insert(orderDTO);
        });
    }
}
