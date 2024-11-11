package org.main.metadata;

import org.main.inverted_index.StoreBinaryFile_II;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class StoreBinaryFile_MD implements StoreInterface_MD {
    private final String METADATA_PATH = "datamart/metadata.bin";

    /*@Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        try (FileOutputStream fileOut = new FileOutputStream(METADATA_PATH);
             GZIPOutputStream gzipOut = new GZIPOutputStream(fileOut);
             ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut)) {

            objectOut.writeObject(metadata);
            objectOut.flush();
            gzipOut.finish();


            System.out.println("Metadata successfully stored in compressed binary format.");
        } catch (IOException e) {
            System.err.println("Error storing metadata to compressed binary file: " + e.getMessage());
        }
    }*/

    @Override
    public void storeMetadata(Map<String, Map<String, String>> metadata) {
        File file = new File(METADATA_PATH);
        boolean append = file.exists(); // Check if the file already exists to avoid overwriting

        try (FileOutputStream fileOut = new FileOutputStream(METADATA_PATH, true);
             StoreBinaryFile_MD.AppendableObjectOutputStream objectOut = new StoreBinaryFile_MD.AppendableObjectOutputStream(fileOut, append)) {

            objectOut.writeObject(metadata);
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