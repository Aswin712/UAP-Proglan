package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KalkulatorUmur {

    // Nama file untuk menyimpan data
    private static final String FILE_NAME = "data.txt";
    // Pola regex untuk validasi nama hanya berupa huruf dan spasi
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Penghitung Umur");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            // Membuat panel untuk menampung elemen GUI
            JPanel panel = new JPanel(new GridLayout(6, 2));

            // Elemen input nama dan tanggal lahir
            JLabel nameLabel = new JLabel("Nama:");
            JTextField nameField = new JTextField();

            JLabel dobLabel = new JLabel("Tanggal Lahir (yyyy-MM-dd):");
            JTextField dobField = new JTextField();

            // Tombol untuk menghitung umur
            JButton calculateButton = new JButton("Hitung Umur");
            JLabel resultLabel = new JLabel("Umur:");

            // Tombol untuk operasi CRUD
            JButton saveButton = new JButton("Simpan Data");
            JButton viewButton = new JButton("Lihat Data");
            JButton updateButton = new JButton("Update Data");
            JButton deleteButton = new JButton("Hapus Data");

            // Area untuk menampilkan data dari file
            JTextArea displayArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(displayArea);
            displayArea.setEditable(false);

            // Event listener untuk menghitung umur
            calculateButton.addActionListener(e -> {
                try {
                    String dobInput = dobField.getText();
                    LocalDate dob = LocalDate.parse(dobInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate now = LocalDate.now();
                    Period age = Period.between(dob, now);
                    resultLabel.setText("Age: " + age.getYears() + " Tahun");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Date Format! Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Event listener untuk menyimpan data
            saveButton.addActionListener(e -> {
                try {
                    String name = nameField.getText();
                    String dob = dobField.getText();
                    if (name.isEmpty() || dob.isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
                        throw new IllegalArgumentException("Name must be alphabetic and Date of Birth cannot be empty.");
                    }
                    saveRecord(name, dob);
                    JOptionPane.showMessageDialog(frame, "Record Saved Successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Event listener untuk membaca data dari file
            viewButton.addActionListener(e -> {
                displayArea.setText("");
                List<String> records = readRecords();
                records.forEach(record -> displayArea.append(record + "\n"));
            });

            // Event listener untuk memperbarui data
            updateButton.addActionListener(e -> {
                try {
                    String name = nameField.getText();
                    String newDob = dobField.getText();
                    if (name.isEmpty() || newDob.isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
                        throw new IllegalArgumentException("Name must be alphabetic and new Date of Birth cannot be empty.");
                    }
                    updateRecord(name, newDob);
                    JOptionPane.showMessageDialog(frame, "Record Updated Successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Event listener untuk menghapus data
            deleteButton.addActionListener(e -> {
                try {
                    String name = nameField.getText();
                    if (name.isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
                        throw new IllegalArgumentException("Name must be alphabetic and cannot be empty.");
                    }
                    deleteRecord(name);
                    JOptionPane.showMessageDialog(frame, "Record Deleted Successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Menambahkan elemen ke panel
            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(dobLabel);
            panel.add(dobField);
            panel.add(calculateButton);
            panel.add(resultLabel);
            panel.add(saveButton);
            panel.add(viewButton);
            panel.add(updateButton);
            panel.add(deleteButton);

            // Menambahkan panel dan scroll pane ke frame
            frame.add(panel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Metode untuk menyimpan data ke file
    public static void saveRecord(String name, String dob) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + "," + dob);
            writer.newLine();
        }
    }

    // Metode untuk membaca semua data dari file
    public static List<String> readRecords() {
        List<String> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException ex) {
            records.add("Error reading records: " + ex.getMessage());
        }
        return records;
    }

    // Metode untuk memperbarui data di file
    public static void updateRecord(String name, String newDob) throws IOException {
        List<String> records = readRecords();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            boolean found = false;
            for (String record : records) {
                String[] parts = record.split(",");
                if (parts[0].equalsIgnoreCase(name)) {
                    writer.write(name + "," + newDob);
                    found = true;
                } else {
                    writer.write(record);
                }
                writer.newLine();
            }
            if (!found) {
                throw new IllegalArgumentException("Record not found for update.");
            }
        }
    }

    // Metode untuk menghapus data dari file
    public static void deleteRecord(String name) throws IOException {
        List<String> records = readRecords();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            boolean found = false;
            for (String record : records) {
                String[] parts = record.split(",");
                if (!parts[0].equalsIgnoreCase(name)) {
                    writer.write(record);
                    writer.newLine();
                } else {
                    found = true;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Record not found for deletion.");
            }

        }
    }
}
