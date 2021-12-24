package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public Long save(Order order) {
        if (duplicateOrder(order)) {
            throw new IllegalStateException("이미 주문이 완료되었습니다.");
        }

        em.persist(order);
        return order.getId();
    }

    private boolean duplicateOrder(Order order) {
        return false;
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order o join o.member m" +
//                        " where  o.status = :status " +
//                        " ", Order.class)
//                .getResultList();
//    }
}
