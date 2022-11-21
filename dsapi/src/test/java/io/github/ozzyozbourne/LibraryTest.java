package io.github.ozzyozbourne;

import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class LibraryTest {

    @Test void gdApiServiceBuilderTestFull() {

            GSapi<?> gsapi = GSapi.Builder.getBuilder(LibraryTest.class)
                    .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0")
                    .setAPPLICATION_NAME("Desktop client 1")
                    .setRESOURCE_CLASS(LibraryTest.class)
                    .setCREDS_STORE("/credstore/creds.json")
                    .setSCOPES(Collections.singletonList(SheetsScopes.SPREADSHEETS))
                    .IS_SERVICE_ACCOUNT(true)
                    .build();

            System.out.println(gsapi);

            List<List<Object>> data  = new ArrayList<>();

            data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
            data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

            data.forEach(s->s.forEach(System.out::println));
            try {
              UpdateValuesResponse updateValuesResponse =  gsapi.update(data, "test!A1:C2");
              System.out.println(updateValuesResponse.toPrettyString());
            }catch (IOException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                Assertions.fail("[ASSERT FAILED] IO Exception");
            }

    }

    @Test void gdApiServiceBuilderTest() {
        GSapi<?> gsapi = GSapi.Builder.getBuilder(LibraryTest.class)
                .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0")
                .setRESOURCE_CLASS(LibraryTest.class)
                .setAPPLICATION_NAME("Desktop client 1").build();

        System.out.println(gsapi);

        List<List<Object>> data  = new ArrayList<>();

        data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
        data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

        data.forEach(s->s.forEach(System.out::println));
        try {
            UpdateValuesResponse updateValuesResponse =  gsapi.update(data, "testTwo!A1:C2");
            System.out.println(updateValuesResponse.toPrettyString());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
    }

    @Test void gdApiAuthBuilderTest() {

        GSapi<?> gsapi = GSapi.Builder.getBuilder(LibraryTest.class)
                .setGOOGLE_SHEETS_ID("1Pmw6JI3Z0Wd5af-ox-D3AnX7fbvUcEhXv7SkpjfiVo0")
                .setAPPLICATION_NAME("Desktop client 1")
                .setRESOURCE_CLASS(LibraryTest.class)
                .setCREDS_STORE("/credstore/auth.json")
                .IS_SERVICE_ACCOUNT(false)
                .build();

        System.out.println(gsapi);

        List<List<Object>> data  = new ArrayList<>();

        data.add(Arrays.asList("faker.animal().name()", "faker.name().firstName()", "faker.yoda().quote()"));
        data.add(Arrays.asList("faker.chuckNorris().fact()", "faker.zelda().character()", "faker.ancient().god()"));

        data.forEach(s->s.forEach(System.out::println));
        try {
            UpdateValuesResponse updateValuesResponse =  gsapi.update(data, "auth!A1:C2");
            System.out.println(updateValuesResponse.toPrettyString());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            Assertions.fail("[ASSERT FAILED] IO Exception");
        }
    }
}
