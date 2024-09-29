package uz.pdp;

import uz.pdp.entity.*;

public class Main {
    public static void main(String[] args) {
        DB.generate();
        BotController botController = new BotController();
        botController.start();

    }
}