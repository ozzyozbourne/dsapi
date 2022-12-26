package io.github.ozzyozbourne;

import io.github.ozzyozbourne.apis.GDCapi;
import org.junit.jupiter.api.Test;

public class LibraryTestDocs {

    @Test
    public void docsTest(){

        GDCapi<?> gdCapi = GDCapi.Builder.getBuilder(LibraryTestDocs.class).build();

    }
}
