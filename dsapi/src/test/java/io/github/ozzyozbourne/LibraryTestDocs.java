package io.github.ozzyozbourne;

import com.google.api.services.docs.v1.model.Document;
import io.github.ozzyozbourne.apis.GDCapi;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LibraryTestDocs {

    final static String docsId = "1D3v-8DYG1PKbYuFc9t8jI6_ryJQwe6cr_xbwYX6KVYA";

    @Test
    public void docsTestService() throws IOException {

        GDCapi<?> gdCapi = GDCapi.Builder.getBuilder(LibraryTestDocs.class).build();
        Document response = gdCapi.docsService.documents().get(docsId).execute();
        String title = response.getTitle();

        System.out.printf("The title of the doc is: %s\n", title);


    }

    @Test
    public void docsTestOAuth() throws IOException {

        GDCapi<?> gdCapi = GDCapi.Builder.getBuilder(LibraryTestDocs.class).IS_SERVICE_ACCOUNT(false)
                .setCREDS_STORE("/credstore/auth.json").build();
        Document response = gdCapi.docsService.documents().get(docsId).execute();
        String title = response.getTitle();

        System.out.printf("The title of the doc is: %s\n", title);


    }
}
