package io.github.ozzyozbourne.apis;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author osaid khan
 * @version 5.0.0
 * @param <T> Class containing the necessary credentials files
 * A CRUD wrapper for Google drive Api that uses Objects as string for all CRUD operations
 */
public class GDapi<T> {
    private final String APPLICATION_NAME;
    private final String TOKENS_DIRECTORY_PATH;
    private final String CREDS_STORE;
    private final JsonFactory JSON_FACTORY;
    private final List<String> SCOPES;
    private final NetHttpTransport netHttpTransport;
    /**
     * Exposing the drive obj so users can build their own custom functions from it
     */
    public  final Drive driveService;
    private final  Class<T> RESOURCE_CLASS;

    private GDapi(Builder<T> builder)  {
        this.APPLICATION_NAME = builder.APPLICATION_NAME;
        this.RESOURCE_CLASS = builder.RESOURCE_CLASS;
        this.TOKENS_DIRECTORY_PATH = builder.TOKENS_DIRECTORY_PATH;
        this.CREDS_STORE = builder.CREDS_STORE;
        this.SCOPES = builder.SCOPES;
        this.JSON_FACTORY = GsonFactory.getDefaultInstance();
        this.netHttpTransport = CommonAuth.getNetHttpTransPort();
        this.driveService = getDSAuth();
    }

    private Drive getDSAuth(){
        return new  Drive.Builder(netHttpTransport, JSON_FACTORY,
                CommonAuth.authorize(RESOURCE_CLASS, CREDS_STORE, JSON_FACTORY, netHttpTransport, SCOPES, TOKENS_DIRECTORY_PATH))
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     *
     * @param pageSize No of Results to be returned
     * @return List of files
     * @throws IOException Throws an exception in case of failure
     */

    public List<File> getFiles(String pageSize) throws IOException {
        return driveService.files().list().setPageSize(40).setFields("nextPageToken, files(id, name)")
                .execute().getFiles();
    }

    /**
     *
     * @param id Name of the resource to be downloaded
     * @param type Type of the resource ie txt, csv etc
     * @param location Location in the users disk to be stored
     * @throws IOException Throws an exception in case of failure
     */

    public void downloadFile(String id, String type, String location)throws IOException {

        try (OutputStream outputStream = Files.newOutputStream(Paths.get(location))){
            driveService.files().export(id, type).executeMediaAndDownloadTo(outputStream);
        }catch (GoogleJsonResponseException e) {
            System.out.println("Error Occurred" + e.getDetails());
            throw e;
    }
    }

    /**
     *
     * @param <T> Class containing the necessary credentials files
     */
    public static class Builder<T>{

        private final Class<T> RESOURCE_CLASS;
        private  String APPLICATION_NAME = "DSApi";
        private  String TOKENS_DIRECTORY_PATH = "tokens_drive";
        private  String CREDS_STORE = "/credstore/auth.json";
        private  List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

        /**
         *
         * @param t Variable of the class of type T
         * @return Buider Object
         * @param <T> Class containing the necessary credentials files
         */
        public static <T> Builder<T> getBuilder(Class<T> t){
            return new Builder<>(t);
        }

        /**
         *
         * @return the Configured Google drive object
         */
        public GDapi<T> build()
        {
            return new GDapi<>(this);
        }

        private Builder(Class<T> RESOURCE_CLASS ) {
            this.RESOURCE_CLASS = RESOURCE_CLASS;
        }

        /**
         *
         * @param APPLICATION_NAME Sets the application name default name is DSApi
         * @return Builder Object
         */
        public Builder<T> setAPPLICATION_NAME(String APPLICATION_NAME) {
            this.APPLICATION_NAME = APPLICATION_NAME;
            return this;
        }

        /**
         *
         * @param TOKENS_DIRECTORY_PATH sets the token directory path DEFAULT is tokens
         * @return Builder Object
         */
        public Builder<T> setTOKENS_DIRECTORY_PATH(String TOKENS_DIRECTORY_PATH) {
            this.TOKENS_DIRECTORY_PATH = TOKENS_DIRECTORY_PATH;
            return this;
        }

        /**
         *
         * @param CREDS_STORE sets the path to Google drive credentials in the resource folder
         * @return Builder Object
         */
        public Builder<T> setCREDS_STORE(String CREDS_STORE) {
            this.CREDS_STORE = CREDS_STORE;
            return this;
        }

        /**
         *
         * @param SCOPES Sets the scope of drive default is read and write both
         * @return Builder Object
         */
        public Builder<T> setSCOPES(List<String> SCOPES) {
            this.SCOPES = SCOPES;
            return this;
        }

    }

    @Override
    public String toString() {
        return "GDapi{" +
                "APPLICATION_NAME='" + APPLICATION_NAME + '\'' +
                ", TOKENS_DIRECTORY_PATH='" + TOKENS_DIRECTORY_PATH + '\'' +
                ", CREDS_STORE='" + CREDS_STORE + '\'' +
                ", JSON_FACTORY=" + JSON_FACTORY +
                ", SCOPES=" + SCOPES +
                ", netHttpTransport=" + netHttpTransport +
                ", driveService=" + driveService +
                ", RESOURCE_CLASS=" + RESOURCE_CLASS +
                '}';
    }
}