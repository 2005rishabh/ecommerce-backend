package com.rishabh.ecommerce.integration;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.rishabh.ecommerce.entities.Product;
import com.rishabh.ecommerce.repositories.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ProductIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    // Arrange
    @BeforeEach
    void setUp() {
        Product p1 = Product.builder()
                .productName("Samsung Galaxy")
                .category("Electronics")
                .price(15000)
                .stock(10)
                .description("Smart phone")
                .build();

        Product p2 = Product.builder()
                .productName("MacBook Air")
                .category("Electronics")
                .price(95000)
                .stock(5)
                .description("Apple laptop")
                .build();

        Product p3 = Product.builder()
                .productName("Levi's Jeans")
                .category("Clothing")
                .price(2500)
                .stock(50)
                .description("Blue denim")
                .build();

        productRepository.saveAll(List.of(p1, p2, p3));
    }

    @Test
    @WithMockUser(roles = "USER") // help to bypass jwt filter
    void shouldReturnPaginatedAndSortedProducts() throws Exception {
        // ACT & ASSERT

        mockMvc.perform(get("/api/products")
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortBy", "price")
                .param("sortDir", "desc"))

                .andExpect(status().isOk())
                // Assert Pagination Metadata
                .andExpect(jsonPath("$.totalElements").value(3)) // 3 products total
                .andExpect(jsonPath("$.pageSize").value(2)) // We requested 2 per page
                .andExpect(jsonPath("$.content.length()").value(2)) // We should only get 2 back

                // Assert Sorting (Descending by Price: MacBook -> Samsung)
                .andExpect(jsonPath("$.content[0].productName").value("MacBook Air"))
                .andExpect(jsonPath("$.content[1].productName").value("Samsung Galaxy"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFilterProductsByCategoryAndPrice() throws Exception {
        // ACT & ASSERT: Filter for Electronics between 10000 and 50000
        mockMvc.perform(get("/api/products")
                .param("category", "Electronics")
                .param("minPrice", "10000")
                .param("maxPrice", "50000"))

                .andExpect(status().isOk())
                // Only Samsung Galaxy should match this exact filter
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].productName").value("Samsung Galaxy"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFilterProductsByKeywordSearch() throws Exception {
        // ACT & ASSERT: Search for keyword "denim"
        mockMvc.perform(get("/api/products")
                .param("keyword", "denim"))

                .andExpect(status().isOk())
                // Only Levi's Jeans should match because "denim" is in its description
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].productName").value("Levi's Jeans"));
    }
}
