package io.github.ozzyozbourne;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LibraryTestDrive {


    @Test
    public void driveTest() throws IOException {
        GDapi<?> gDapi = GDapi.Builder.getBuilder(LibraryTestDrive.class).IS_SERVICE_ACCOUNT(false)
                .setCREDS_STORE("/credstore/auth.json").setSCOPES( Collections.singletonList( DriveScopes.DRIVE_METADATA_READONLY))
                .build();

        FileList result = gDapi.driveService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }

        }
    }


    @Test
    public void driveTestServiceAccount() throws IOException {
        GDapi<?> gDapi = GDapi.Builder.getBuilder(LibraryTestDrive.class)
                .setSCOPES( Collections.singletonList( DriveScopes.DRIVE_METADATA_READONLY))
                .build();

        FileList result = gDapi.driveService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }

        }
    }
}
