package org.twovarchar.violetGoblin;

import org.twovarchar.violetGoblin.player.PlayerService;

import java.util.Scanner;

public class Application {
    private static final PlayerService ps = new PlayerService();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("************************************");
        System.out.println("*   아오오니 초기 스토리라인 따오기    *");
        System.out.println("*          따와서 시뮬               *");
        System.out.println("************************************");

        ps.initMap("MasterRoom");

        char input;

        while (true) {
            input = sc.next().toUpperCase().charAt(0);

            if (input == 'Q') {
                System.exit(0);
            }

            switch (input) {
                case 'W', 'A', 'S', 'D' -> ps.movePlayer(input);
                default -> System.out.println("Invalid input. Use W, A, S, D to move, Q to quit.");
            }

            if (!ps.writeMap()) {
                System.out.println("GAME OVER");
                System.exit(0);
            }
        }
    }
}
