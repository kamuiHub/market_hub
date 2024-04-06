package com.teamchallenge.markethub;

import com.teamchallenge.markethub.dto.authorization.AuthorizationRequest;
import com.teamchallenge.markethub.dto.item.NewItemRequest;
import com.teamchallenge.markethub.dto.order.OrderItemDataRequest;
import com.teamchallenge.markethub.dto.order.OrderRequest;
import com.teamchallenge.markethub.model.OrderItemData;
import com.teamchallenge.markethub.model.enums.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class MarketHubJsonTest {

    @Autowired
    private JacksonTester<AuthorizationRequest> jsonAuth;

    @Autowired
    private JacksonTester<OrderRequest> jsonOrder;

    @Autowired
    private JacksonTester<NewItemRequest> jsonItem;

    private AuthorizationRequest authorizationRequest;
    private OrderRequest orderRequest;
    private NewItemRequest newItemRequest;

    @BeforeEach
    public void setup() {
        authorizationRequest = new AuthorizationRequest("Bilbo",
                "Baggins", "bilbo@gmail.com", "380991419249", "pass123");

        OrderItemDataRequest item1 = new OrderItemDataRequest();
        item1.setItemId(128L);
        item1.setName("Ноутбук LENOVO IdeaPad 3");
        item1.setQuantity(1);
        item1.setPrice(BigDecimal.valueOf(29900));
        item1.setCategoryId(100L);
        item1.setSubCategoryId(100L);

        orderRequest = OrderRequest.builder()
                .customerFirstname("Bilbo")
                .customerLastname("Baggins")
                .customerEmail("bilbo@gmail.com")
                .customerPhone("09347726144")
                .customerCity("Hobbiton")
                .items(List.of(item1))
                .totalQuantity(1)
                .deliveryService(DeliveryService.NOVA_POSHTA)
                .postalAddress("Backer street 221b")
                .totalAmount(29900)
                .build();

        newItemRequest = new NewItemRequest("Name","Description", 100.00, "Brand",
                List.of("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAADFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P"),
                10,1L,100L,100L);


    }

    @Test
    public void authRequestSerializationTest() throws IOException {
        assertThat(jsonAuth.write(authorizationRequest)).isStrictlyEqualToJson("authorizationRequest.json");
        assertThat(jsonAuth.write(authorizationRequest)).extractingJsonPathStringValue("@.firstname")
                .isEqualTo("Bilbo");
        assertThat(jsonAuth.write(authorizationRequest)).extractingJsonPathStringValue("@.email")
                .isEqualTo("bilbo@gmail.com");
        assertThat(jsonAuth.write(authorizationRequest)).extractingJsonPathStringValue("@.password")
                .isEqualTo("pass123");
    }

    @Test
    public void authRequestDeserializationTest() throws IOException {
        String expected = """
                {
                   "firstname": "Bilbo",
                   "lastname": "Baggins",
                   "email": "bilbo@gmail.com",
                   "phone": "380991419249",
                   "password": "pass123"
                }
                """;
        assertThat(jsonAuth.parse(expected))
                .isEqualTo(authorizationRequest);
        assertThat(jsonAuth.parseObject(expected).email()).isEqualTo("bilbo@gmail.com");
        assertThat(jsonAuth.parseObject(expected).password()).isEqualTo("pass123");
    }


    @Test
    public void orderRequestSerializationTest() throws IOException {
        assertThat(jsonOrder.write(orderRequest)).isStrictlyEqualToJson("orderRequest.json");
    }

    @Test
    public void orderRequestDeserializationTest() throws IOException {
        String expected = """
                {
                    "customer_firstname": "Bilbo",
                    "customer_lastname": "Baggins",
                    "customer_email": "bilbo@gmail.com",
                    "customer_phone": "09347726144",
                    "customer_city": "Hobbiton",
                    "items": [
                      {
                        "item_id": "128",
                        "name": "Ноутбук LENOVO IdeaPad 3",
                        "quantity": 1,
                        "price": 29900,
                        "category_id": 100,
                        "sub_category_id": 100
                      }
                    ],
                    "total_quantity": 1,
                    "delivery_service": "NOVA_POSHTA",
                    "postal_address": "Backer street 221b",
                    "total_amount": 29900
                }
                """;
        assertThat(jsonOrder.parse(expected))
                .isEqualTo(orderRequest);
    }

    @Test
    public void newItemRequestSerializationTest() throws IOException {
        assertThat(jsonItem.write(newItemRequest)).isStrictlyEqualToJson("newItemRequest.json");
        assertThat(jsonItem.write(newItemRequest)).extractingJsonPathStringValue("@.name")
                .isEqualTo("Name");
        assertThat(jsonItem.write(newItemRequest)).extractingJsonPathNumberValue("@.price")
                .isEqualTo(100.00);
        assertThat(jsonItem.write(newItemRequest)).extractingJsonPathNumberValue("@.owner")
                .isEqualTo(1);
        assertThat(jsonItem.write(newItemRequest)).extractingJsonPathArrayValue("@.photos")
                .isEqualTo(List.of("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAADFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P"));
    }

    @Test
    public void newItemRequestDeserializationTest() throws IOException {
        String expected = """
                {
                   "name": "Name",
                   "description": "Description",
                   "price": 100.00,
                   "brand": "Brand",
                   "photos": [
                     "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAADFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P"
                   ],
                   "stock_quantity": 10,
                   "owner": 1,
                   "category": 100,
                   "sub_category": 100
                 }
                """;
        assertThat(jsonItem.parse(expected))
                .isEqualTo(newItemRequest);
    }
}
