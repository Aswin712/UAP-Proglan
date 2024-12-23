    package org.example;

    import org.junit.jupiter.api.*;
    import java.io.*;
    import java.nio.file.*;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;

    public class KalkulatorUmurTest {

        private static final String FILE_NAME = "data.txt";

        @BeforeEach
        public void setUp() throws IOException {
            // Bersihkan file data.txt sebelum setiap tes
            Files.deleteIfExists(Paths.get(FILE_NAME));
        }

        @Test
        public void testSaveRecord() throws IOException {
            // Simulasikan menyimpan data
            KalkulatorUmur.saveRecord("John Doe", "1980-05-15");

            // Periksa apakah data berhasil disimpan
            List<String> records = KalkulatorUmur.readRecords();
            assertEquals(1, records.size(), "There should be one record saved.");
            assertTrue(records.get(0).contains("John Doe"), "Record should contain 'John Doe'");
        }

        @Test
        public void testReadRecords() throws IOException {
            // Simulasikan beberapa penyimpanan data
            KalkulatorUmur.saveRecord("Alice", "1990-03-12");
            KalkulatorUmur.saveRecord("Bob", "1985-07-19");

            // Periksa apakah semua data bisa dibaca
            List<String> records = KalkulatorUmur.readRecords();
            assertEquals(2, records.size(), "There should be two records.");
            assertTrue(records.get(0).contains("Alice"), "First record should be Alice.");
            assertTrue(records.get(1).contains("Bob"), "Second record should be Bob.");
        }

        @Test
        public void testUpdateRecord() throws IOException {
            // Simulasikan menyimpan data
            KalkulatorUmur.saveRecord("Charlie", "1992-11-25");

            // Periksa apakah data berhasil disimpan
            List<String> recordsBeforeUpdate = KalkulatorUmur.readRecords();
            assertEquals(1, recordsBeforeUpdate.size(), "Record count should be 1 before update.");

            // Memperbarui data
            KalkulatorUmur.updateRecord("Charlie", "1992-12-15");

            // Periksa apakah data berhasil diperbarui
            List<String> recordsAfterUpdate = KalkulatorUmur.readRecords();
            assertEquals(1, recordsAfterUpdate.size(), "Record count should be 1 after update.");
            assertTrue(recordsAfterUpdate.get(0).contains("1992-12-15"), "Date of birth should be updated.");
        }

        @Test
        public void testDeleteRecord() throws IOException {
            // Simulasikan menyimpan data
            KalkulatorUmur.saveRecord("David", "2000-02-28");

            // Periksa apakah data berhasil disimpan
            List<String> recordsBeforeDelete = KalkulatorUmur.readRecords();
            assertEquals(1, recordsBeforeDelete.size(), "Record count should be 1 before delete.");

            // Menghapus data
            KalkulatorUmur.deleteRecord("David");

            // Periksa apakah data berhasil dihapus
            List<String> recordsAfterDelete = KalkulatorUmur.readRecords();
            assertEquals(0, recordsAfterDelete.size(), "Record count should be 0 after delete.");
        }

        @Test
        public void testDeleteNonExistentRecord() {
            // Coba menghapus data yang tidak ada
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                KalkulatorUmur.deleteRecord("NonExistent");
            });
            assertTrue(exception.getMessage().contains("Record not found for deletion"));
        }

        @Test
        public void testUpdateNonExistentRecord() {
            // Coba memperbarui data yang tidak ada
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                KalkulatorUmur.updateRecord("NonExistent", "1995-01-01");
            });
            assertTrue(exception.getMessage().contains("Record not found for update"));
        }

        @AfterEach
        public void tearDown() throws IOException {
            // Bersihkan file setelah setiap tes
            Files.deleteIfExists(Paths.get(FILE_NAME));
        }
    }
