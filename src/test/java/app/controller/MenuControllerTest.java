//package app.controller;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class MenuControllerTest {
//
//    @Autowired
//    MockMvc mvc;
//
//    @Test
//    @DisplayName("menu 진입")
//    void helloWorld() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/"))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/html/menu.html"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("management 진입")
//    void admin() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/management"))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/html/management.html"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("code 입력 진입")
//    void adminCodes() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/management/codes"))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/html/codes.html"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Admin mornitering 진입")
//    void adminAll() {
//        try {
//            mvc.perform(MockMvcRequestBuilders.get("/others"))
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/html/all.html"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//}