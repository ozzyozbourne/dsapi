package io.github.ozzyozbourne;

import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import io.github.ozzyozbourne.apis.GMapi;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class LibraryTestGmail {


    @Test
    public void driveTestRead() throws IOException {
        GMapi<?> gMapi = GMapi.Builder.getBuilder(LibraryTestDrive.class).build();

        String user = "me";
        ListLabelsResponse listResponse = gMapi.gmailService.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }

        }
    }
}
