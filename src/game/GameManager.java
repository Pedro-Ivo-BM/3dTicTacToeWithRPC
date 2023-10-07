package game;

public class GameManager {
    private int[][][] cells = new int[3][3][3];

    private static final int[][][] WINNING_LINES = {
            // Linhas horizontais
            { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 2, 0 } },
            { { 0, 0, 1 }, { 0, 1, 1 }, { 0, 2, 1 } },
            { { 0, 0, 2 }, { 0, 1, 2 }, { 0, 2, 2 } },
            { { 1, 0, 0 }, { 1, 1, 0 }, { 1, 2, 0 } },
            { { 1, 0, 1 }, { 1, 1, 1 }, { 1, 2, 1 } },
            { { 1, 0, 2 }, { 1, 1, 2 }, { 1, 2, 2 } },
            { { 2, 0, 0 }, { 2, 1, 0 }, { 2, 2, 0 } },
            { { 2, 0, 1 }, { 2, 1, 1 }, { 2, 2, 1 } },
            { { 2, 0, 2 }, { 2, 1, 2 }, { 2, 2, 2 } },

            // Linhas verticais
            { { 0, 0, 0 }, { 1, 0, 0 }, { 2, 0, 0 } },
            { { 0, 0, 1 }, { 1, 0, 1 }, { 2, 0, 1 } },
            { { 0, 0, 2 }, { 1, 0, 2 }, { 2, 0, 2 } },
            { { 0, 1, 0 }, { 1, 1, 0 }, { 2, 1, 0 } },
            { { 0, 1, 1 }, { 1, 1, 1 }, { 2, 1, 1 } },
            { { 0, 1, 2 }, { 1, 1, 2 }, { 2, 1, 2 } },
            { { 0, 2, 0 }, { 1, 2, 0 }, { 2, 2, 0 } },
            { { 0, 2, 1 }, { 1, 2, 1 }, { 2, 2, 1 } },
            { { 0, 2, 2 }, { 1, 2, 2 }, { 2, 2, 2 } },

            // Linhas de profundidade
            { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 2 } },
            { { 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 2 } },
            { { 2, 0, 0 }, { 2, 0, 1 }, { 2, 0, 2 } },
            { { 0, 1, 0 }, { 0, 1, 1 }, { 0, 1, 2 } },
            { { 1, 1, 0 }, { 1, 1, 1 }, { 1, 1, 2 } },
            { { 2, 1, 0 }, { 2, 1, 1 }, { 2, 1, 2 } },
            { { 0, 2, 0 }, { 0, 2, 1 }, { 0, 2, 2 } },
            { { 1, 2, 0 }, { 1, 2, 1 }, { 1, 2, 2 } },
            { { 2, 2, 0 }, { 2, 2, 1 }, { 2, 2, 2 } },

            // Diagonais dentro de cada camada
            { { 0, 0, 0 }, { 1, 1, 0 }, { 2, 2, 0 } },
            { { 0, 2, 0 }, { 1, 1, 0 }, { 2, 0, 0 } },
            { { 0, 0, 1 }, { 1, 1, 1 }, { 2, 2, 1 } },
            { { 0, 2, 1 }, { 1, 1, 1 }, { 2, 0, 1 } },
            { { 0, 0, 2 }, { 1, 1, 2 }, { 2, 2, 2 } },
            { { 0, 2, 2 }, { 1, 1, 2 }, { 2, 0, 2 } },

            // Diagonais que cruzam camadas
            { { 0, 0, 0 }, { 1, 1, 1 }, { 2, 2, 2 } },
            { { 0, 2, 0 }, { 1, 1, 1 }, { 2, 0, 2 } },
            { { 2, 0, 0 }, { 1, 1, 1 }, { 0, 2, 2 } },
            { { 2, 2, 0 }, { 1, 1, 1 }, { 0, 0, 2 } },
            { { 0, 0, 0 }, { 0, 1, 1 }, { 0, 2, 2 } },
            { { 0, 2, 0 }, { 0, 1, 1 }, { 0, 0, 2 } },
            { { 1, 0, 0 }, { 1, 1, 1 }, { 1, 2, 2 } },
            { { 1, 2, 0 }, { 1, 1, 1 }, { 1, 0, 2 } },
            { { 2, 0, 0 }, { 2, 1, 1 }, { 2, 2, 2 } },
            { { 2, 2, 0 }, { 2, 1, 1 }, { 2, 0, 2 } },
            { { 0, 0, 0 }, { 1, 0, 1 }, { 2, 0, 2 } },
            { { 0, 2, 0 }, { 1, 2, 1 }, { 2, 2, 2 } },
            { { 2, 0, 0 }, { 1, 0, 1 }, { 0, 0, 2 } },
            { { 2, 2, 0 }, { 1, 2, 1 }, { 0, 2, 2 } }

    };

    public GameManager() {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                for (int z = 0; z < 3; z++)
                    cells[x][y][z] = 0;
    }

    public boolean checkVictory(int player) {
        for (int[][] line : WINNING_LINES) {
            if (cells[line[0][0]][line[0][1]][line[0][2]] == player &&
                    cells[line[1][0]][line[1][1]][line[1][2]] == player &&
                    cells[line[2][0]][line[2][1]][line[2][2]] == player)
                return true;
        }
        return false;
    }

    public boolean makePlay(int z, int x, int y, int player) {
        if (cells[z][x][y] == 0) {
            cells[z][x][y] = player;
            return true;
        }
        return false;

    }

}
