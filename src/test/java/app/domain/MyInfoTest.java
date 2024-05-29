package app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyInfoTest {

    @Autowired
    private MyInfo myInfo;

    @DisplayName("Test getInfo")
    @Test
    public void testGetInfo() {
        Info info = myInfo.getInfo();
        assert info.getX() == 15 : info.toString();
        assert info.getY() == 20 : info.toString();
        assert info.getId().equals("team5") : info.toString();
        assert info.getIp().equals("127.0.0.1") : info.toString();
        assert info.getPort() == 11120 : info.toString();
    }
}
