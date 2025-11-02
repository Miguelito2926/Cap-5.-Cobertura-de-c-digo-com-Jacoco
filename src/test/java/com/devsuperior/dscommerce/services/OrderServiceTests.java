package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.tests.OrderFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuthService authService;

    private Long existingOrderId, nonExistsOrderId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createCustomAdmintUser(1L, "Ed");
        client = UserFactory.createCustomClientUser(2L, "Miguel");
        order = OrderFactory.createOrder(client);
        orderDTO = new OrderDTO(order);

        Mockito.when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.findById(nonExistsOrderId)).thenReturn(Optional.empty());
    }
}
