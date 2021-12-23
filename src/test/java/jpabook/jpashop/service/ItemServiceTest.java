package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    // 상품 등록 테스트
    @Test
    @DisplayName("상품 등록 테스트")
    void itemJoinTest() {
        // given
        Item item = new Book();
        item.setName("JPA Book1");
        item.setPrice(30000);
        item.setStockQuantity(100);

        // when
        itemService.saveItem(item);

        // then
        assertThat(item).isEqualTo(itemRepository.findAll().stream().findAny().get());
    }

    // 상품 단건 조회 테스트
    @Test
    @DisplayName("상품 단건 조회 테스트")
    void findOneTest() {
        // given
        Item item = new Book();
        item.setName("Spring Book");
        item.setStockQuantity(200);
        item.setPrice(50000);

        // when
        itemService.saveItem(item);
        Item findItem = itemService.findOne(item.getId());

        // then
        assertThat(item).isEqualTo(findItem);
    }

    // 상품 전체 조회 테스트
    @Test
    @DisplayName("상품 전체 조회 테스트")
    void findAllTest() {
        // given
        Item item = new Book();
        item.setId(1L);
        item.setName("Spring Book");
        item.setStockQuantity(200);
        item.setPrice(50000);

        Item item2 = new Book();
        item2.setName("JPA Book1");
        item2.setPrice(30000);
        item2.setStockQuantity(100);

        // when
        itemService.saveItem(item);
        itemService.saveItem(item2);
        List<Item> findItems = itemService.findItems();

        // then
        assertThat(findItems.size()).isEqualTo(2);
    }
}