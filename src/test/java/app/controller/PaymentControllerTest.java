//package app.controller;
//
//import app.repository.CodeRepository;
//import app.repository.ItemRepository;
//import app.service.CommunicationService;
//import app.service.ManagementService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class PaymentControllerTest {
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
//    @Autowired
//    private CodeRepository codeRepository;
//
//    @BeforeAll
//    static void insertCode(){
////        codeRepository
//    }
//
//    @Test
//    void pay() {
//        String requestJson = "{\"itemCode\":1,\"cardNumber\":1234,\"quantity\":2}";
//        try {
//            mvc.perform(MockMvcRequestBuilders.post("/payment/pay")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestJson))
//                    .andDo(print())
//                    .andExpect(status().isOk());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    void prepay() {
//        String requestJson = "{\"itemCode\":1,\"cardNumber\":1234,\"quantity\":2}";
//        try {
//            mvc.perform(MockMvcRequestBuilders.post("/payment/prepay")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestJson))
//                    .andDo(print())
//                    .andExpect(status().isOk());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test   //미완
//    void pickup() {
//        String certCode = "A1234";
//        try {
//            mvc.perform(MockMvcRequestBuilders.post("/payment/pickup")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(certCode))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect((ResultMatcher) jsonPath("$.name == 콜라"))
//                    .andExpect((ResultMatcher) jsonPath("$.quantity == 2"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//}