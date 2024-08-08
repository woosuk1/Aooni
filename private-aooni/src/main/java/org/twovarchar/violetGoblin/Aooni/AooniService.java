package org.twovarchar.violetGoblin.Aooni;

import org.twovarchar.violetGoblin.player.PlayerRepository;
import org.twovarchar.violetGoblin.room.Room;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class AooniService {
    private static int aooniCurX;
    private static int aooniCurY;

    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
    private static int[][] visited;
    private static int[][][] predecessor;
    private static final Queue<int[]> queue = new ArrayDeque<>();

    public AooniService() {
    }

    public static int getAooniCurX() {
        return aooniCurX;
    }

    public static void setAooniCurX(int aooniCurX) {
        AooniService.aooniCurX = aooniCurX;
    }

    public static int getAooniCurY() {
        return aooniCurY;
    }

    public static void setAooniCurY(int aooniCurY) {
        AooniService.aooniCurY = aooniCurY;
    }

    public static boolean crashPlayer() {
        int playerX = PlayerRepository.getPlayerCurX();
        int playerY = PlayerRepository.getPlayerCurY();
        if (bfs(aooniCurX, aooniCurY, playerX, playerY)) {
            backTrack(aooniCurX, aooniCurY, playerX, playerY);
            int distance = Math.abs(playerY - aooniCurY) + Math.abs(playerX - aooniCurX);
            return distance >= 2;
        }
        return false;
    }

    private static boolean bfs(int startX, int startY, int goalX, int goalY) {
        visited = new int[Room.mapSize][Room.mapSize];
        predecessor = new int[Room.mapSize][Room.mapSize][2];
        for (int[] row : visited) {
            Arrays.fill(row, -1);
        }
        for (int i = 0; i < predecessor.length; i++) {
            for (int j = 0; j < predecessor[i].length; j++) {
                predecessor[i][j][0] = -1;
                predecessor[i][j][1] = -1;
            }
        }

        queue.clear();
        queue.offer(new int[]{startX, startY});
        visited[startY][startX] = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == goalX && y == goalY) {
                return true; // 목표 위치에 도달
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isValidMove(nx, ny) && visited[ny][nx] == -1) {
                    visited[ny][nx] = visited[y][x] + 1;
                    queue.offer(new int[]{nx, ny});
                    predecessor[ny][nx][0] = x;
                    predecessor[ny][nx][1] = y;
                }
            }
        }
        return false; // 목표 위치에 도달할 수 없음
    }

    private static void backTrack(int startX, int startY, int goalX, int goalY) {
        int tempX = goalX;
        int tempY = goalY;
        int ansY, ansX;
        while (predecessor[tempY][tempX][0] != startX || predecessor[tempY][tempX][1] != startY) {
            ansY = predecessor[tempY][tempX][1];
            ansX = predecessor[tempY][tempX][0];
            tempY = ansY;
            tempX = ansX;
        }
        setAooniCurX(tempX);
        setAooniCurY(tempY);
    }

    private static boolean isValidMove(int x, int y) {
        return x >= 0 && x < Room.mapSize && y >= 0 && y < Room.mapSize;
    }
}
