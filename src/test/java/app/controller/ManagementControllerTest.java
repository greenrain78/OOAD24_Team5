//package app.controller;
//
//import app.repository.ItemRepository;
//import app.service.CommunicationService;
//import app.service.ManagementService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class ManagementControllerTest {
//
//    @Autowired
//    MockMvc mvc;
//
//    @Autowired
//    private ManagementService managementService;
//    @Autowired
//    private CommunicationService communicationService;
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @Test
//    void getAllItems() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/items"))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$").exists());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    void getItemByItemCode() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/item/1"))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.quantity").exists());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    void getItemsByDVM() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/item/1"))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.quantity").exists());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    //socket 이용
////    @Test
////    void getItemByDVM() {
////    }
//
//    @Test
//    void updateItem() {
//        String requestJson = "{\"itemCode\":1, \"quantity\":2}";
//        try {
//            mvc.perform(MockMvcRequestBuilders.put("/item/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestJson))
//                    .andDo(print())
//                    .andExpect(status().isOk());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        Assertions.assertEquals(2, itemRepository.findByItemCode(1).getQuantity());
//    }
//
//    @Test
//    void getAllCodes() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/item/1"))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.quantity").exists());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//}