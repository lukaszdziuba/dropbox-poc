package com.devfordevs.dropboxpoc;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 *
 */
public class DropboxPOC {

    private static final String KEY_FILE = "d:/temp/dropboxpoc/key.txt";

    /**
     * Gets access token from the file
     *
     * @return
     * @throws IOException
     */
    private static String getAccessToken() throws IOException {
        return Files.readAllLines(Paths.get(KEY_FILE)).get(0);
    }

    /**
     * Read InputStream to String
     *
     * @param input
     * @return
     * @throws IOException
     */
    private static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Displays name and content of the file
     *
     * @param name
     * @param content
     */
    private static void showFileContent(String name, String content) {
        System.out.println("File: " + name);
        System.out.println(content);
        System.out.println("------------------------------------");
    }

    public static void main(String[] args) throws DbxException, IOException {
        // Create Dropbox client
        DbxRequestConfig config = new DbxRequestConfig("dropboxpoc", "en_US");
        DbxClientV2 client = new DbxClientV2(config, getAccessToken());
        ListFolderResult result = client.files().listFolder("");

        for (Metadata entry : result.getEntries()) {
            System.out.println(entry.getPathDisplay());
            if (entry.getName().endsWith("csv")) {
                // display only content of *.csv file
                InputStream fileStream = client.files().download(entry.getPathLower()).getInputStream();
                String content = read(fileStream);
                showFileContent(entry.getName(), content);
            }
        }
    }
}
