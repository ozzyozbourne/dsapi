package io.github.ozzyozbourne;

import com.google.api.services.drive.model.File;
import io.github.ozzyozbourne.apis.GDapi;
import io.github.ozzyozbourne.enums.GDEnums;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class LibraryTestDrive {


    @Test
    public void driveTestRead() throws IOException {
        GDapi<?> gDapi = GDapi.Builder.getBuilder(LibraryTestDrive.class).build();

        List<File> files = gDapi.getFiles("40");
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
        gDapi.downloadFile("1SUtwQlc_quPwMIthI9dOZwBA1QBxNPNE7eL84XXlcBA", GDEnums.DOCX.toString() ,
                "src/test/resources/drivedownloads/LOR_Osaid Khan.docx");

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
