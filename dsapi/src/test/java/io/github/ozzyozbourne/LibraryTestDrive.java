package io.github.ozzyozbourne;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class LibraryTestDrive {


    @Test
    public void driveTestRead() throws IOException {
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


    @Test
    public void driveTestDowload() throws IOException {
        GDapi<?> gDapi = GDapi.Builder.getBuilder(LibraryTestDrive.class).build();
        try {
            OutputStream outputStream = Files.newOutputStream(Paths.get("src/test/resources/drivedownloads/thefilename.xlsx"));
                gDapi.driveService.files().export("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0",
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .executeMediaAndDownloadTo(outputStream);
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to move file: " + e.getDetails());
            throw e;
        }

    }

    @Test
    public void enumTest(){
        System.out.println(  GDEnums.PDF);
        System.out.println(  GDEnums.CSV);
        System.out.println(  GDEnums.PNG);
        System.out.println(  GDEnums.SVG);
        System.out.println(  GDEnums.DOCX);
        System.out.println(  GDEnums.XLSX);
    }
}
