package org.twovarchar.violetGoblin.player;

public class PlayerService {
    private static final PlayerRepository pr = new PlayerRepository();

    public PlayerService() {
    }

    public void initMap(String mapName) {
        System.out.println("-------------아오오니-------------");
        pr.printMap(mapName);
    }

    public boolean writeMap() {
        return pr.printMap(pr.getCurrentMapKey());
    }

    public void movePlayer(char cmd) {
        pr.checkPlayerState(pr.controlPlayer(cmd));
    }
}
