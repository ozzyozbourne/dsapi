package io.github.ozzyozbourne;

import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class LibraryTest {

    DSapi<?> dsapi = DSapi.Builder.getBuilder(LibraryTest.class)
            .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0")
            .build();

    @Test void gdApiServiceBuilderTestFull() {

            DSapi<?> dsapi = DSapi.Builder.getBuilder(LibraryTest.class)
                    .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0")
                    .setAPPLICATION_NAME("Desktop client 1")
                    .setCREDS_STORE("/credstore/creds.json")
                    .setSheetsSCOPES(Collections.singletonList(SheetsScopes.SPREADSHEETS))
                    .IS_SERVICE_ACCOUNT(true)
                    .build();

            System.out.println(dsapi);

            List<List<Object>> data  = new ArrayList<>();

            data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
            data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

            data.forEach(s->s.forEach(System.out::println));
            try {
              UpdateValuesResponse updateValuesResponse =  dsapi.update(data, "test!A1:C2");
              System.out.println(updateValuesResponse.toPrettyString());
            }catch (IOException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                Assertions.fail("[ASSERT FAILED] IO Exception");
            }

    }

    @Test void gdApiServiceBuilderTest() {
        DSapi<?> dsapi = DSapi.Builder.getBuilder(LibraryTest.class)
                .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0").build();

        System.out.println(dsapi);

        List<List<Object>> data  = new ArrayList<>();

        data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
        data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

        data.forEach(s->s.forEach(System.out::println));
        try {
            UpdateValuesResponse updateValuesResponse =  dsapi.update(data, "testTwo!A1:C2");
            System.out.println(updateValuesResponse.toPrettyString());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
    }

    @Test void gdApiAuthBuilderTest() {

        DSapi<?> dsapi = DSapi.Builder.getBuilder(LibraryTest.class)
                .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0")
                .setAPPLICATION_NAME("Desktop client 1")
                .setCREDS_STORE("/credstore/auth.json")
                .IS_SERVICE_ACCOUNT(false)
                .build();

        System.out.println(dsapi);

        List<List<Object>> data  = new ArrayList<>();

        data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
        data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

        data.forEach(s->s.forEach(System.out::println));
        try {
            UpdateValuesResponse updateValuesResponse =  dsapi.update(data, "auth!A1:C2");
            System.out.println(updateValuesResponse.toPrettyString());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
    }

    @Test void gdApiReadTest() {

        try {
            ValueRange valueRange = dsapi.read("test!A1:C2");
            System.out.println(valueRange);
            valueRange.getValues().forEach(System.out::println);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }

    }

    @Test void gdApiDeleteAndReadTest() {
        final String loc = "test!A1:C2";
        update(loc);
        read(loc);
        delete(loc);
        readEmpty(loc);
        System.out.println("DELETE OPERATION SUCCESS");
    }

    @Test void gdApiCreateSheetTest() throws IOException {
        DSapi<?> dsapi = DSapi.Builder.getBuilder(LibraryTest.class)
                .IS_SERVICE_ACCOUNT(false)
                .setCREDS_STORE("/credstore/auth.json")
                .build();
        List<String> tabName  = Arrays.asList("TestOne", "TestTwo", "TestThree", "TestFour");
       Spreadsheet spreadsheet =  dsapi.createNewSpreadSheet("Tester", tabName);
       Assertions.assertNotNull(spreadsheet);
        System.out.println(spreadsheet);

    }

    private void update(String loc){
        List<List<Object>> data  = new ArrayList<>();

        data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
        data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

        data.forEach(s->s.forEach(System.out::println));
        try {
            UpdateValuesResponse updateValuesResponse =  dsapi.update(data, loc);
            System.out.println(updateValuesResponse.toPrettyString());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
    }

    private void delete(String loc){
        List<List<Object>>  data  = new ArrayList<>();

        data.add(Arrays.asList("", "", ""));
        data.add(Arrays.asList("", "", ""));

        try {
            UpdateValuesResponse updateValuesResponse =  dsapi.update(data, loc);
            System.out.println("VALUES DELETED");
            System.out.println(updateValuesResponse.toPrettyString());
        }catch (IOException e){
            e.printStackTrace();
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
    }

    private void readEmpty(String loc) {
        try {
            ValueRange valueRange = dsapi.read(loc);
            System.out.println(valueRange);
            Assertions.assertNull(valueRange.getValues());
        }catch (IOException e){
            e.printStackTrace();
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
        System.out.println("EMPTY READ OPERATION SUCCESS");
    }

    private void read(String loc) {
        try {
            ValueRange valueRange = dsapi.read(loc);
            System.out.println(valueRange);
            Assertions.assertNotNull(valueRange.getValues());
            valueRange.getValues().forEach(System.out::println);
        }catch (IOException e){
            e.printStackTrace();
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
        System.out.println("READ OPERATION SUCCESS");
    }
}
