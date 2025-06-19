import javax.swing.*;              // Import library GUI (Swing)
import java.awt.*;                // Import library grafis (layout, color, dsb)
import java.awt.event.*;          // Import library untuk menangani event (klik, dll)

public class KnightTourUI extends JFrame {
    private final int SIZE = 8;                   // Ukuran papan (8x8)
    private JButton[][] cells = new JButton[SIZE][SIZE]; // Matriks tombol sebagai papan
    private int[][] board = new int[SIZE][SIZE];         // Matriks status langkah knight
    // Gerakan kuda dalam catur (dx dan dy untuk x dan y)
    private final int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    private final int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    private boolean solving = false;     // Status apakah sedang dalam mode solving otomatis
    private boolean paused = false;      // Status apakah sedang pause
    private boolean manualMode = false;  // Status apakah dalam mode manual

    private JButton resetButton, pauseButton, modeButton; // Tombol kontrol

    private int currentX = -1, currentY = -1; // Posisi terakhir knight
    private int moveCount = 1;                // Hitungan langkah knight

    public KnightTourUI() {
        // Pengaturan awal window
        setTitle("Knight's Tour - Solver & Manual");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel papan (grid 8x8)
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));

        // Buat tombol-tombol papan dan atur warna & aksi klik
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 18));
                btn.setFocusPainted(false);
                // Warna papan catur (abu dan biru)
                btn.setBackground((row + col) % 2 == 0 ? new Color(240, 240, 240) : new Color(100, 149, 237));
                int r = row, c = col;
                btn.addActionListener(e -> onCellClick(r, c)); // Event klik pada sel
                cells[row][col] = btn;
                boardPanel.add(btn);
            }
        }

        // Panel kontrol bawah
        JPanel controlPanel = new JPanel();

        // Tombol Reset
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBackground(new Color(255, 87, 87)); // Merah
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> {
            if (!solving) resetBoard(); // Reset hanya jika tidak sedang solving
        });

        // Tombol Pause
        pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 16));
        pauseButton.setBackground(new Color(87, 160, 255)); // Biru
        pauseButton.setForeground(Color.WHITE);
        pauseButton.addActionListener(e -> {
            paused = !paused;
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        // Tombol ganti mode manual/otomatis
        modeButton = new JButton("Switch to Manual Mode");
        modeButton.setFont(new Font("Arial", Font.BOLD, 16));
        modeButton.setBackground(new Color(120, 200, 120)); // Hijau
        modeButton.setForeground(Color.BLACK);
        modeButton.addActionListener(e -> {
            manualMode = !manualMode;
            modeButton.setText(manualMode ? "Switch to Auto Mode" : "Switch to Manual Mode");
            resetBoard(); // Reset saat ganti mode
        });

        // Tambahkan tombol ke panel kontrol
        controlPanel.add(resetButton);
        controlPanel.add(pauseButton);
        controlPanel.add(modeButton);

        // Tambahkan panel ke frame utama
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Set ukuran, lokasi tengah, dan tampilkan
        setSize(600, 680);
        setLocationRelativeTo(null);
        setVisible(true);

        // Inisialisasi papan
        resetBoard();
    }

    // Reset papan ke kondisi awal
    private void resetBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = -1;

        solving = false;
        paused = false;
        moveCount = 1;
        currentX = currentY = -1;
        pauseButton.setText("Pause");
        updateBoard();
    }

    // Event klik pada sel papan
    private void onCellClick(int r, int c) {
        if (manualMode) {
            // Mode manual: pilih langkah satu per satu
            if (moveCount == 1) {
                board[r][c] = moveCount++;
                currentX = c;
                currentY = r;
                updateBoard();
            } else {
                if (isValidManualMove(c, r)) {
                    board[r][c] = moveCount++;
                    currentX = c;
                    currentY = r;
                    updateBoard();
                } else {
                    JOptionPane.showMessageDialog(this, "Langkah tidak sah!");
                }
            }
        } else {
            // Mode otomatis: mulai solving dari klik awal
            if (!solving) {
                resetBoard();
                board[r][c] = 1;
                currentX = c;
                currentY = r;
                solving = true;
                new Thread(() -> {
                    if (solve(currentX, currentY, 2)) {
                        System.out.println("Selesai!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Tidak ada solusi dari posisi ini.");
                    }
                    solving = false;
                }).start();
            }
        }
    }

    // Validasi langkah manual apakah sah sebagai langkah kuda
    private boolean isValidManualMove(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE || board[y][x] != -1) return false;
        for (int i = 0; i < 8; i++) {
            if (currentX + dx[i] == x && currentY + dy[i] == y) return true;
        }
        return false;
    }

    // Update tampilan papan berdasarkan status board
    private void updateBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton btn = cells[i][j];
                int val = board[i][j];
                if (val == -1) {
                    btn.setText("");
                    btn.setBackground((i + j) % 2 == 0 ? new Color(240, 240, 240) : new Color(100, 149, 237));
                } else {
                    btn.setText(String.valueOf(val));
                    btn.setBackground(new Color(255, 215, 0)); // Kuning untuk langkah
                }
            }
        }
    }

    // Cek apakah posisi valid dan belum dikunjungi
    private boolean isValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[y][x] == -1;
    }

    // Fungsi solving dengan heuristik Warnsdorff (cari langkah dengan aksesibilitas paling kecil)
    private boolean solve(int x, int y, int moveCount) {
        try { Thread.sleep(250); } catch (InterruptedException ignored) {} // Delay antar langkah
        while (paused) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {} // Tunggu jika pause
        }

        updateBoard();
        if (moveCount > SIZE * SIZE) return true; // Selesai semua kotak

        int minDeg = 9;
        int nextX = -1, nextY = -1;

        // Coba semua langkah, pilih dengan tingkat aksesibilitas terendah
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValid(nx, ny)) {
                int deg = getAccessibility(nx, ny);
                if (deg < minDeg) {
                    minDeg = deg;
                    nextX = nx;
                    nextY = ny;
                }
            }
        }

        if (nextX == -1) return false; // Tidak ada langkah valid

        board[nextY][nextX] = moveCount;
        if (solve(nextX, nextY, moveCount + 1)) return true; // Rekursi langkah berikutnya
        board[nextY][nextX] = -1; // Backtrack jika gagal
        updateBoard();
        return false;
    }

    // Hitung jumlah langkah yang tersedia dari posisi tertentu
    private int getAccessibility(int x, int y) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isValid(nx, ny)) count++;
        }
        return count;
    }

    // Fungsi main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(KnightTourUI::new); // Jalankan UI di thread event
    }
}
