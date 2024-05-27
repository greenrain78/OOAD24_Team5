package app.repository;

import app.domain.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Objects;

@DataJpaTest
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;
    @DisplayName("item 저장 테스트")
    @Test
    public void saveItem() {
        // 초기 값이 있는지 확인
        assert !itemRepository.findAll().isEmpty() : "실제 값 : " + itemRepository.findAll();
        assert itemRepository.findAll().size() == 20 : "실제 값 : " + itemRepository.findAll().size() + " / " + itemRepository.findAll();
        // 저장
        Item testItem = new Item("testItem1", 900, 900);
        itemRepository.save(testItem);
        // 저장된 데이터가 맞는지 확인
        assert itemRepository.findAll().size() == 21 : "실제 값 : " + itemRepository.findAll().size();
        Item savedItem = itemRepository.findByItemCode(900);
        assert savedItem.getName().equals("testItem1") : "실제 값 : " + savedItem.getName();
        assert savedItem.getQuantity() == 900 : "실제 값 : " + savedItem.getQuantity();
        assert savedItem.getItemCode() == 900 : "실제 값 : " + savedItem.getItemCode();
    }
    @DisplayName("item 개수 수정 테스트")
    @Test
    public void updateItem() {
        // 초기 값이 있는지 확인
        assert !itemRepository.findAll().isEmpty() : "실제 값 : " + itemRepository.findAll();
        assert itemRepository.findAll().size() == 20 : "실제 값 : " + itemRepository.findAll().size() + " / " + itemRepository.findAll();
        // 저장
        Item testItem = new Item("testItem1", 900, 900);
        itemRepository.save(testItem);
        // 저장된 데이터가 맞는지 확인
        assert itemRepository.findAll().size() == 21 : "실제 값 : " + itemRepository.findAll().size();
        Item savedItem = itemRepository.findByItemCode(900);
        assert savedItem.getName().equals("testItem1") : "실제 값 : " + savedItem.getName();
        assert savedItem.getQuantity() == 900 : "실제 값 : " + savedItem.getQuantity();
        assert savedItem.getItemCode() == 900 : "실제 값 : " + savedItem.getItemCode();
        // 수정
        Item updateItem = itemRepository.findByItemCode(900);
        updateItem.setQuantity(1000);
        // 수정된 데이터가 맞는지 확인
        assert itemRepository.findAll().size() == 21 : "실제 값 : " + itemRepository.findAll().size();
        Item updatedItem = itemRepository.findByItemCode(900);
        assert updatedItem.getName().equals("testItem1") : "실제 값 : " + updatedItem.getName();
        assert updatedItem.getQuantity() == 1000 : "실제 값 : " + updatedItem.getQuantity();
        assert updatedItem.getItemCode() == 900 : "실제 값 : " + updatedItem.getItemCode();

    }

    @DisplayName("ItemCode로 조회 테스트")
    @Test
    public void findByItemCode() {
        // 조회
        Item item = itemRepository.findByItemCode(1);
        // 조회된 데이터가 맞는지 확인
        assert item.getName().equals("콜라") : "item.getName() 실제 값 : " + item.getName();
        assert item.getQuantity() == 10 : "item.getQuantity() 실제 값 : " + item.getQuantity();
        assert item.getItemCode() == 1 : "item.getItemCode() 실제 값 : " + item.getItemCode();
    }
    @DisplayName("ItemCode로 조회시 오류 테스트")
    @Test
    public void findByItemCodeError() {
        // 조회
        Item item = itemRepository.findByItemCode(1000);
        // 조회된 데이터가 맞는지 확인
        assert Objects.isNull(item);
    }
}
