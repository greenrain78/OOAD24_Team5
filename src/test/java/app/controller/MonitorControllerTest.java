package app.controller;

import app.domain.Info;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MonitorControllerTest {

    @Autowired
    MockMvc mvc;

    Gson gson;

    MonitorControllerTest(){
        this.gson = new Gson();
    }

    @Test
    void getAllDVM() {
        try {
            mvc.perform(MockMvcRequestBuilders.post("/monitor/client")
                            .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(new Info("team5", "127.0.0.1", 11120)))
            );
            mvc.perform(MockMvcRequestBuilders.get("/monitor/clients"))
                    .andDo(print())
                    .andExpect((ResultMatcher) jsonPath("$[?(@.id == %s)]", "team53").exists())
                    .andExpect(status().isOk());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getMyInfo() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/monitor/info"))
                    .andDo(print())
                    .andExpect((ResultMatcher) jsonPath("$.id == %s", "team5").exists())
                    .andExpect(status().isOk());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}