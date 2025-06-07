/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TicTacToe;

public class Board {
    private int[][] data;   // menyimpan nilai 0 / 1 / -1
    private int turn;       // menyimpan nilai 1 / -1

    /*
    constructor
    inisialisasi board dengan nilai 0
    dan turn dengan 1 atau -1 sesuai input user
    */
    public Board(int turn ){
        this.data = new int[3][3];
        this.turn = turn;
    }

    // cetak board di layar
    public void disp(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                switch(this.data[i][j]){
                    case 0  -> System.out.print("  -  ");
                    case -1 -> System.out.print("  X  ");
                    case 1  -> System.out.print("  O  ");
                }
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    /*
    setting board pada baris dan kolom yang telah ditentukan
    dengan nilai 1 atau -1 sesuai dengan giliran/ turn
    */
    public boolean setBoard(int brs, int kol){
        if (this.data[brs][kol] == 0) {
            this.data[brs][kol] = turn;
            turn = -turn;
            return true;
        } else {
            return false;
        }
    }

    // Metode overload: set board dengan nilai player tertentu (tanpa mengganti giliran internal)
    public boolean setBoard(int brs, int kol, int player){
        if (this.data[brs][kol] == 0) {
            this.data[brs][kol] = player;
            return true;
        } else {
            return false;
        }
    }

    // Metode helper untuk percobaan langkah (digunakan AI)
    public boolean setBoardMove(int brs, int kol, int player) {
        return setBoard(brs, kol, player);
    }

    // Reset satu kotak menjadi kosong
    public void resetCell(int brs, int kol) {
        this.data[brs][kol] = 0;
    }

    // Cek apakah sel kosong
    public boolean isCellEmpty(int brs, int kol) {
        return this.data[brs][kol] == 0;
    }

    public int winner(){
        // cek baris
        for (int i = 0; i < 3; i++){
            int sum = data[i][0] + data[i][1] + data[i][2];
            if(sum == 3) return 1;  // O menang
            if(sum == -3) return -1; // X menang
        }

        // cek kolom
        for (int j = 0; j < 3; j++){
            int sum = data[0][j] + data[1][j] + data[2][j];
            if(sum == 3) return 1;
            if(sum == -3) return -1;
        }

        // cek diagonal utama
        int sumDiagonal1 = data[0][0] + data[1][1] + data[2][2];
        if(sumDiagonal1 == 3) return 1;
        if(sumDiagonal1 == -3) return -1;

        // cek diagonal kedua
        int sumDiagonal2 = data[0][2] + data[1][1] + data[2][0];
        if(sumDiagonal2 == 3) return 1;
        if(sumDiagonal2 == -3) return -1;

        // tidak ada pemenang
        return 0;
    }

    public boolean gameOver(){
        // jika sudah ada pemenang
        if (winner() != 0) {
            return true;
        }
        // cek apakah ada kotak kosong
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(data[i][j] == 0){
                    return false;
                }
            }
        }
        // semua kotak terisi → seri
        return true;
    }

    public void resetBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.data[i][j] = 0;
            }
        }
    }

    // Metode untuk mendapatkan giliran internal (dipakai di TicTacToe)
    public int getTurn(){
        return this.turn;
    }

    // Metode untuk mengatur giliran internal
    public void setTurn(int turn){
        this.turn = turn;
    }
}
