
package TicTacToe;
import java.util.Scanner;
import java.util.Random;

import java.util.Scanner;
import java.util.Random;

public class TicTacToe {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        
        boolean playAgain = true;
        while (playAgain) {
            // Pilih tingkat kesulitan
            System.out.println("Pilih tingkat kesulitan:");
            System.out.println("1. Mudah");
            System.out.println("2. Normal");
            System.out.println("3. Susah Banget");
            int difficulty = 0;
            while (true) {
                if (scanner.hasNextInt()) {
                    difficulty = scanner.nextInt();
                    if (difficulty >= 1 && difficulty <= 3) break;
                    else System.out.println("Harus angka 1, 2, atau 3.");
                } else {
                    System.out.println("Masukkan angka yang valid!");
                    scanner.next(); // Buang input yang tidak valid
                }
            }
            scanner.nextLine(); // Bersihkan newline setelah input angka
            
            // Tentukan giliran pertama secara acak
            int turn = rand.nextBoolean() ? -1 : 1;  // -1 untuk user (X), 1 untuk komputer (O)
            Board x = new Board(turn);
            
            System.out.println("\nPermainan dimulai! Anda: X, Komputer: O.");
            System.out.println((turn == -1 ? "Kamu mulai duluan." : "Komputer mulai duluan.") + "\n");
            x.disp();
            
            boolean gameEnded = false;
            while (!gameEnded) {
                if (x.getTurn() == -1) {
                    // Giliran pemain
                    System.out.print("Giliran X (Anda). Masukkan baris dan kolom (1-3) dipisahkan spasi: ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        System.out.println("Input tidak boleh kosong, coba lagi.");
                        continue;
                    }
                    
                    String[] parts = input.split("\\s+");
                    if (parts.length != 2) {
                        System.out.println("Input harus dua angka dipisahkan spasi (misal: 1 1). Coba lagi.");
                        continue;
                    }
                    
                    int brs, kol;
                    try {
                        brs = Integer.parseInt(parts[0]) - 1;
                        kol = Integer.parseInt(parts[1]) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Input harus berupa angka (misal: 1 1). Coba lagi.");
                        continue;
                    }
                    
                    if (brs < 0 || brs > 2 || kol < 0 || kol > 2) {
                        System.out.println("Angka harus antara 1 dan 3.");
                        continue;
                    }
                    
                    if (!x.setBoard(brs, kol, -1)) {
                        System.out.println("Kotak sudah terisi, coba lagi.");
                        continue;
                    }
                    
                    x.setTurn(1); // Ubah giliran ke komputer
                } else {
                    // Giliran komputer
                    System.out.println("Giliran Komputer (O)...");
                    if (difficulty == 1) {
                        makeRandomMove(x);
                    } else if (difficulty == 2) {
                        makeNormalMove(x);
                    } else {
                        makeSmartMove(x);
                    }
                    x.setTurn(-1); // Ubah giliran ke pemain
                }
                
                x.disp();
                
                int hasil = x.winner();
                if (hasil != 0 || x.gameOver()) {
                    if (hasil == -1)
                        System.out.println("Pemenangnya adalah: Kamu (X)");
                    else if (hasil == 1)
                        System.out.println("Pemenangnya adalah: Komputer (O)");
                    else
                        System.out.println("Permainan berakhir seri.");
                    gameEnded = true;
                }
            }
            
            System.out.print("\nIngin main lagi? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            while (response.isEmpty()) {  // pastikan input tidak kosong
                response = scanner.nextLine().trim().toLowerCase();
            }
            playAgain = response.equals("y") || response.equals("ya");
            
            x.resetBoard();
            System.out.println("\nPapan di-reset.\n");
        }
        
        System.out.println("Terima kasih sudah bermain!");
        scanner.close();
    }

    // Metode AI tingkat mudah: gerakan acak
    private static void makeRandomMove(Board x) {
        Random rand = new Random();
        int brs, kol;
        do {
            brs = rand.nextInt(3);
            kol = rand.nextInt(3);
        } while (!x.setBoard(brs, kol, 1));
    }

    // Metode AI tingkat normal: coba blokir jika lawan hampir menang
    private static void makeNormalMove(Board x) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (x.isCellEmpty(i, j)) {
                    x.setBoard(i, j, -1);
                    if (x.winner() == -1) {
                        x.resetCell(i, j);
                        x.setBoard(i, j, 1);
                        return;
                    }
                    x.resetCell(i, j);
                }
            }
        }
        makeRandomMove(x);
    }

    // Metode AI tingkat susah: coba menang terlebih dahulu, jika tidak, blokir lawan, jika tidak pilih acak
    private static void makeSmartMove(Board x) {
        // Coba langkah untuk menang
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (x.isCellEmpty(i, j)){
                    x.setBoard(i, j, 1);
                    if (x.winner() == 1){
                        return;
                    }
                    x.resetCell(i, j);
                }
            }
        }
        // Coba blokir lawan
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (x.isCellEmpty(i, j)){
                    x.setBoard(i, j, -1);
                    if (x.winner() == -1){
                        x.resetCell(i, j);
                        x.setBoard(i, j, 1);
                        return;
                    }
                    x.resetCell(i, j);
                }
            }
        }
        // Jika tidak ada langkah menang atau blokir, pilih gerakan acak
        makeRandomMove(x);
    }
}
