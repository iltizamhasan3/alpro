package game_tongkat;

import java.util.Scanner;
import java.util.Random;

public class GameTongkat {
    // Konstanta untuk maksimal tongkat yang bisa diambil per giliran
    private static final int MAX_TAKE = 3;
    
    // Variabel game state
    private int totalSticks;       // Jumlah tongkat tersisa
    private boolean userTurn;      // True jika giliran user, false jika komputer
    private Scanner scanner;       // Untuk input user
    private Random random;         // Untuk generate angka acak
    private StringBuilder gameHistory; // Menyimpan history permainan
    private Difficulty difficulty; // Tingkat kesulitan yang dipilih

    // Enum untuk tingkat kesulitan beserta range tongkat
    private enum Difficulty {
        EASY(10, 15),    // Easy: 10-15 tongkat
        MEDIUM(16, 30),  // Medium: 16-30 tongkat
        HARD(30, 45);    // Hard: 30-45 tongkat
        
        final int minSticks;  // Minimum tongkat
        final int maxSticks;  // Maksimum tongkat
        
        // Constructor untuk enum Difficulty
        Difficulty(int min, int max) {
            this.minSticks = min;
            this.maxSticks = max;
        }
    }

    // Constructor utama
    public GameTongkat() {
        scanner = new Scanner(System.in);  // Inisialisasi scanner
        random = new Random();             // Inisialisasi random
        gameHistory = new StringBuilder(); // Inisialisasi history
    }

    // Method utama untuk memulai game
    public void startGame() {
        // Menampilkan header game
        System.out.println("=== GAME TONGKAT TERAKHIR ===");
        System.out.println("Ambil 1-3 tongkat setiap giliran. Yang mengambil tongkat terakhir KALAH!\n");
        
        // Loop utama game
        do {
            selectDifficulty();  // Memilih tingkat kesulitan
            initializeGame();     // Inisialisasi game baru
            playGame();          // Memulai permainan
        } while (askPlayAgain()); // Ulangi jika user ingin main lagi
        
        // Penutup
        System.out.println("\nTerima kasih telah bermain!");
        scanner.close(); // Tutup scanner
    }

    // Method untuk memilih tingkat kesulitan
    private void selectDifficulty() {
        System.out.println("\nPilih tingkat kesulitan:");
        System.out.println("1. Easy (10-15 batang)");
        System.out.println("2. Medium (16-30 batang)");
        System.out.println("3. Hard (30-45 batang)");
        System.out.print("Masukkan pilihan (1-3): ");
        
        int choice = 0;
        try {
            // Membaca input user
            choice = scanner.nextInt();
            // Validasi input
            if (choice < 1 || choice > 3) {
                System.out.println("Masukkan angka antara 1-3!");
                scanner.nextLine(); // Bersihkan buffer
                selectDifficulty(); // Rekursi jika input invalid
                return;
            }
        } catch (Exception e) {
            // Handle input non-angka
            System.out.println("Input tidak valid! Masukkan angka.");
            scanner.nextLine(); // Bersihkan buffer
            selectDifficulty(); // Rekursi
            return;
        }
        
        // Set difficulty berdasarkan pilihan
        switch(choice) {
            case 1:
                difficulty = Difficulty.EASY;
                break;
            case 2:
                difficulty = Difficulty.MEDIUM;
                break;
            case 3:
                difficulty = Difficulty.HARD;
                break;
        }
    }

    // Method untuk inisialisasi game baru
    private void initializeGame() {
        // Acak jumlah tongkat berdasarkan difficulty
        totalSticks = difficulty.minSticks + random.nextInt(difficulty.maxSticks - difficulty.minSticks + 1);
        // Acak giliran pertama
        userTurn = random.nextBoolean();
        // Reset history
        gameHistory.setLength(0);
        
        // Tampilkan info permainan
        System.out.println("\nPermainan dimulai! Jumlah tongkat: " + totalSticks);
        System.out.println("Tingkat kesulitan: " + difficulty.name());
        if (userTurn) {
            System.out.println("Anda dapat giliran pertama!");
        } else {
            System.out.println("Komputer dapat giliran pertama!");
        }
    }

    // Method utama untuk bermain
    private void playGame() {
        // Loop selama masih ada tongkat
        while (totalSticks > 0) {
            printSticks(); // Tampilkan sisa tongkat
            
            // Pilih method berdasarkan giliran
            if (userTurn) {
                userMove();    // Giliran user
            } else {
                computerMove(); // Giliran komputer
            }
            
            // Ganti giliran
            userTurn = !userTurn;
        }
        
        // Tampilkan pemenang ketika game selesai
        declareWinner();
    }

    // Method untuk giliran user
    private void userMove() {
        System.out.print("\nAmbil tongkat (1-3): ");
        int take = 0;
        
        try {
            // Baca input user
            take = scanner.nextInt();
            // Validasi input
            if (take < 1 || take > MAX_TAKE || take > totalSticks) {
                System.out.println("Masukkan angka antara 1-3 dan tidak melebihi tongkat tersedia!");
                scanner.nextLine(); // Bersihkan buffer
                userMove();        // Rekursi jika input invalid
                return;
            }
        } catch (Exception e) {
            // Handle input non-angka
            System.out.println("Input tidak valid! Masukkan angka.");
            scanner.nextLine(); // Bersihkan buffer
            userMove();        // Rekursi
            return;
        }
        
        // Kurangi tongkat dan tambahkan ke history
        totalSticks -= take;
        gameHistory.append("Anda mengambil ").append(take).append(" tongkat\n");
    }

    // Method untuk giliran komputer
    private void computerMove() {
        // Hitung jumlah tongkat yang diambil komputer
        int take = calculateComputerMove();
        // Kurangi tongkat
        totalSticks -= take;
        
        // Tampilkan aksi komputer dan tambahkan ke history
        System.out.println("\nKomputer mengambil " + take + " tongkat");
        gameHistory.append("Komputer mengambil ").append(take).append(" tongkat\n");
    }

    // Method untuk menghitung langkah komputer
    private int calculateComputerMove() {
        // Strategi untuk memaksa user mengambil tongkat terakhir
        int remaining = totalSticks % (MAX_TAKE + 1);
        // Jika sisa 1, ambil 1
        // Jika sisa 0, ambil MAX_TAKE (3)
        // Else ambil remaining-1
        return (remaining == 1) ? 1 : (remaining == 0) ? MAX_TAKE : remaining - 1;
    }

    // Method untuk menampilkan sisa tongkat
    private void printSticks() {
        System.out.println("\nTongkat tersisa: " + totalSticks);
    }

    // Method untuk menampilkan hasil akhir
    private void declareWinner() {
        System.out.println("\n=== HASIL ===");
        // Tampilkan history permainan
        System.out.println(gameHistory.toString());
        
        // Tentukan pemenang
        if (!userTurn) {
            System.out.println("Anda mengambil tongkat terakhir. Anda KALAH!");
        } else {
            System.out.println("Komputer mengambil tongkat terakhir. Anda MENANG!");
        }
    }

    // Method untuk menanyakan main lagi
    private boolean askPlayAgain() {
    while (true) {
        System.out.print("\nMain lagi? (y/n): ");
        String input = scanner.next().toLowerCase();
        
        if (input.equals("y")) return true;
        if (input.equals("n")) return false;
        
        System.out.println("Input tidak dikenali! Masukkan hanya 'y' atau 'n'.");
    }
}

    // Main method
    public static void main(String[] args) {
        new GameTongkat().startGame(); // Mulai game
    }
}
