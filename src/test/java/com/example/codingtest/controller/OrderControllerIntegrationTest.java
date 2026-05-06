package com.example.codingtest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.cache.type=simple",
        "app.kafka.enabled=false"
})
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crud() throws Exception {
        String createRequest = """
                {
                  "userId": 1,
                  "items": [
                    {
                      "productName": "apple",
                      "price": 1000,
                      "quantity": 2
                    },
                    {
                      "productName": "banana",
                      "price": 500,
                      "quantity": 3
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/2"))
                .andExpect(jsonPath("$.orderId").value(2))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.totalPrice").value(3500));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        String updateRequest = """
                {
                  "userId": 2,
                  "items": [
                    {
                      "productName": "orange",
                      "price": 700,
                      "quantity": 4
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/api/orders/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id").isNumber())
                .andExpect(jsonPath("$.items[0].productName").value("orange"))
                .andExpect(jsonPath("$.totalPrice").value(2800));

        mockMvc.perform(delete("/api/orders/2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/orders/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createFailsWhenRequestIsInvalid() throws Exception {
        String invalidRequest = """
                {
                  "userId": 1,
                  "items": [
                    {
                      "productName": "",
                      "price": 1000,
                      "quantity": 1
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("items[0].productName: must not be blank"));
    }
}
