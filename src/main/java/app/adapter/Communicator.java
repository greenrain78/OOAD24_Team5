package app.adapter;

import app.domain.Info;

import java.util.ArrayList;
import java.util.List;

public class Communicator {
    private final List<Info> availableDVMs = new ArrayList<>();
    private final Info myInfo;

    public Communicator(Info myInfo) {
        this.myInfo = myInfo;
    }
    public Info getMyInfo() {
        return myInfo;
    }
}
