package org.twovarchar.violetGoblin.room;

public class RightRoom implements Room{

    protected char roomState = 'r';

    public char getRoomState() {
        return roomState;
    }

    public void setRoomState(char roomState) {
        this.roomState = roomState;
    }

    @Override
    public String[][] createMap() {
        int mapSizeR = Room.mapSize;
        String[][] setRoom = new String[mapSizeR][mapSizeR];

        for(int i=0; i<mapSizeR; i++){
            for(int j=0; j<mapSizeR; j++){
                /* 설명. 왼쪽에 출구 생성 */
                if((i== (mapSizeR/ 2)) && (j== 0)){
                    setRoom[i][j] = "O";
                    continue;
                }
                /* 설명. 테두리 설정 */
                if(i==0 || j==0 || i== mapSizeR -1 || j == mapSizeR -1){
                    setRoom[i][j] = "#";
                }
                else
                    setRoom[i][j] = "*";
            }
            System.out.println();
        }
        return setRoom;
    }

    @Override
    public int getFloor() {
        return 0;
    }

    @Override
    public void setFloor(int floor) {

    }


}
