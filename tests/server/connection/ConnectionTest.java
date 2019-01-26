package server.connection;

import org.junit.jupiter.api.Test;
import server.parts.Game;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {

    @Test
    void checkWin() {
        int [][] desk = {
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}, {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}, {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}, {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,1,-1,-1,-1,-1,-1}, {-1,-1,-1,-1,-1,0,-1,-1,-1,-1}, {-1,-1,-1,-1,1,-1,-1,-1,-1,-1}, {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}, {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},};

        try {
            boolean connection = new Connection(new Game(new Socket())).checkWin(desk, 0);
            assertFalse(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}