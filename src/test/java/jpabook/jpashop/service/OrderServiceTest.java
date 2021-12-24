package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    EntityManager em;
    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문 테스트")
    void createOrderTest() {
        // given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울시", "노원로", "564"));
        em.persist(member);

        Item item = new Book();
        item.setName("JPA BOOK");
        item.setPrice(10000);
        item.setStockQuantity(10);
        em.persist(item);

        int orderCnt = 2;
        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCnt);
        Order getOrder = orderRepository.findOne(orderId);

        // then
        assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus());
        assertThat(1).isEqualTo(getOrder.getOrderItems().size());
        assertThat(10000 * 2).isEqualTo(getOrder.getTotalPrice());
        assertThat(8).isEqualTo(item.getStockQuantity());
    }

    @Test
    @DisplayName("상품 주문 재고 수량 초과 테스트")
    void createOrderOutofStock() {
        // given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울시", "노원로", "564"));
        em.persist(member);

        Item item = new Book();
        item.setName("JPA BOOK");
        item.setPrice(10000);
        item.setStockQuantity(10);
        em.persist(item);

        NotEnoughStockException e = assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), 11));

        assertThat(e.getMessage()).isEqualTo("need more stock");
    }

    @Test
    @DisplayName("상품 취소 테스트")
    void cancelOrderTest() {
        // given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울시", "노원로", "564"));
        em.persist(member);

        Item item = new Book();
        item.setName("JPA BOOK");
        item.setPrice(10000);
        item.setStockQuantity(10);
        em.persist(item);

        int orderCnt = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCnt);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());
        assertThat(10).isEqualTo(item.getStockQuantity());
    }
}