package org.main.inverted_index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class StoreBinaryFile_II implements StoreInterface_II{
    private final String INVERTED_INDEX_PATH = "datamart/inverted_index.bin";
    /*@Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        try (FileOutputStream fileOut = new FileOutputStream(INVERTED_INDEX_PATH);
             GZIPOutputStream gzipOut = new GZIPOutputStream(fileOut);
             ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut)) {

            objectOut.writeObject(invertedIndex);
            objectOut.flush();
            gzipOut.finish();

            System.out.println("Inverted index successfully stored in compressed binary format.");
        } catch (IOException e) {
            System.err.println("Error storing inverted index to compressed binary file: " + e.getMessage());
        }
    }*/

    @Override
    public void storeInvertedIndex(Map<String, Map<String, List<Integer>>> invertedIndex) {
        File file = new File(INVERTED_INDEX_PATH);
        boolean append = file.exists(); // Check if the file already exists to avoid overwriting

        try (FileOutputStream fileOut = new FileOutputStream(INVERTED_INDEX_PATH, true);
             AppendableObjectOutputStream objectOut = new AppendableObjectOutputStream(fileOut, append)) {

            objectOut.writeObject(invertedIndex);
            System.out.println("Inverted index successfully appended to binary file.");

        } catch (IOException e) {
            System.err.println("Error appending inverted index to binary file: " + e.getMessage());
        }
    }

    // Custom ObjectOutputStream to prevent re-writing header if appending
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(FileOutputStream out, boolean append) throws IOException {
            super(out);
            if (append) {
                reset(); // Skip writing header if we are appending to an existing file
            } else {
                writeStreamHeader(); // Write header only if new file
            }
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // Override to control when the header is written
            super.writeStreamHeader();
        }
    }
}