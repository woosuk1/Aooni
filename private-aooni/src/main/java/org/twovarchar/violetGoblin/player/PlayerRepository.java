package org.twovarchar.violetGoblin.player;

import org.twovarchar.violetGoblin.Aooni.AooniService;
import org.twovarchar.violetGoblin.room.*;
import org.twovarchar.violetGoblin.room.Left.*;
import org.twovarchar.violetGoblin.room.Right.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerRepository {
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, String[][]> maps = new HashMap<>();
    private String[][] currentMap;
    private String currentMapKey;
    private Room currentRoom;
    private static int playerCurX = Room.mapSize / 2;
    private static int playerCurY = Room.mapSize - 2;
    private int roomChangeCount = 0;
    private boolean isAooniAlive = false;

    public PlayerRepository() {
        initializeRooms();
        initializeMaps();
    }

    private void initializeRooms() {
        rooms.put("MasterRoom", new MasterRoom());
        rooms.put("GuestRoom", new GuestRoom());
        rooms.put("HallwayRoom", new HallwayRoom());
        rooms.put("AtticRoom", new AtticRoom());
        rooms.put("LaboratoryRoom", new LaboratoryRoom());
        rooms.put("MainRoom", new MainRoom());
        rooms.put("StoreRoom", new StoreRoom());
        rooms.put("KitchenRoom", new KitchenRoom());
        rooms.put("TerraceRoom", new TerraceRoom());
        rooms.put("TortureRoom", new TortureRoom());
        rooms.put("SecretRoom", new SecretRoom());
    }

    private void initializeMaps() {
        for (String key : rooms.keySet()) {
            maps.put(key, rooms.get(key).createMap());
        }
    }

    public boolean printMap(String key) {
        updateState(key);

        if (isAooniAlive && !AooniService.crashPlayer()) {
            return false;
        }

        System.out.println(" ~ " + currentMapKey + " ~\n");

        for (int i = 0; i < currentMap.length; i++) {
            System.out.print(" ");
            for (int j = 0; j < currentMap[i].length; j++) {
                if (isAooniAlive && i == AooniService.getAooniCurY() && j == AooniService.getAooniCurX()) {
                    System.out.print("A");
                } else if (i == playerCurY && j == playerCurX) {
                    System.out.print("P");
                } else {
                    System.out.print(currentMap[i][j]);
                }
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("W: 상, A: 좌, S: 하, D: 우");

        return true;
    }

    private void updateState(String key) {
        currentRoom = rooms.get(key);
        currentMap = maps.get(key);
        currentMapKey = key;
    }

    public int[] controlPlayer(char cmd) {
        int[] change = new int[2];

        switch (cmd) {
            case 'W' -> change[1] = -1;
            case 'S' -> change[1] = 1;
            case 'A' -> change[0] = -1;
            case 'D' -> change[0] = 1;
        }

        return change;
    }

    public void checkPlayerState(int[] change) {
        int newX = playerCurX + change[0];
        int newY = playerCurY + change[1];

        if (isAtExit()) {
            if (moveToNewRoom(newX, newY)) {
                roomChangeCount++;

                if (isAooniAlive && roomChangeCount == 1) {
                    isAooniAlive = false;
                    roomChangeCount = 0;
                    return;
                }

                if (roomChangeCount == 3) {
                    isAooniAlive = true;
                    setAooniLocation();
                    System.out.println("----------------------------------\n" +
                            "----------!watch out!-------------\n" +
                            "-----ao-oni is coming out!!!!-----\n" +
                            "----------------------------------");
                }
                return;
            }
        }

        if (isWall(newX, newY)) {
            if (isExit(newX, newY)) {
                playerCurX = newX;
                playerCurY = newY;
            }
            return;
        }

        playerCurX = newX;
        playerCurY = newY;
    }

    private void setAooniLocation() {
        if (playerCurX != Room.mapSize / 2) {
            AooniService.setAooniCurX(Room.mapSize / 2);
        } else {
            AooniService.setAooniCurX(Room.mapSize / 2 - 1);
        }
        AooniService.setAooniCurY(1);
    }

    private boolean isAtExit() {
        return currentMap[playerCurY][playerCurX].equals("E");
    }

    private boolean moveToNewRoom(int newX, int newY) {
        /* 설명. 나중에 추가할 것들*/
        String newRoomKey = String.valueOf(currentRoom.getRoomState());
        if (newRoomKey != null) {
            updateState(newRoomKey);
            playerCurX = Room.mapSize - newX - 1;
            playerCurY = Room.mapSize - newY - 1;
            return true;
        }
        return false;
    }

    private boolean isWall(int newX, int newY) {
        return currentMap[newY][newX].equals("■");
    }

    private boolean isExit(int newX, int newY) {
        return currentMap[newY][newX].equals("E");
    }

    public String getCurrentMapKey() {
        return currentMapKey;
    }

    public static int getPlayerCurX() {
        return playerCurX;
    }

    public static int getPlayerCurY() {
        return playerCurY;
    }
}
