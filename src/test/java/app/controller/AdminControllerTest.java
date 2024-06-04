package app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void getAllDVM() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/admin/clients"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getMyInfo() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/admin/info"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}