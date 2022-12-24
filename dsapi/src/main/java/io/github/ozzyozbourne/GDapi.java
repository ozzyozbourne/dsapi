package io.github.ozzyozbourne;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author osaid khan
 * @version 4.0.0
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
        this.netHttpTransport = getNetHttpTransPort();
        this.driveService = builder.IS_SERVICE_ACCOUNT?getDSService():getDSAuth();
    }
    private NetHttpTransport getNetHttpTransPort(){
        NetHttpTransport netHttpTransport;
        try {
            netHttpTransport =  GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return Objects.requireNonNull(netHttpTransport);
    }

    private Credential authorize(){

        Credential credential;
        try(InputStream inputStream = Objects.requireNonNull(RESOURCE_CLASS.getResourceAsStream(CREDS_STORE))){
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.
                    load(JSON_FACTORY, new InputStreamReader(Objects.requireNonNull(inputStream)));
            GoogleAuthorizationCodeFlow flow  = new GoogleAuthorizationCodeFlow.Builder(netHttpTransport , JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return Objects.requireNonNull(credential);
    }

    private Drive getDSAuth(){
        return new  Drive.Builder(netHttpTransport, JSON_FACTORY, authorize()).setApplicationName(APPLICATION_NAME).build();
    }

    private Drive getDSService(){
        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(Objects.requireNonNull(RESOURCE_CLASS.getResourceAsStream(CREDS_STORE)));
            credentials.createScoped(SCOPES).refreshIfExpired();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }return new Drive.Builder(netHttpTransport, JSON_FACTORY,
                new HttpCredentialsAdapter(Objects.requireNonNull(credentials)))
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     *
     * @param <T> Class containing the necessary credentials files
     */
    public static class Builder<T>{

        private final Class<T> RESOURCE_CLASS;
        private  String APPLICATION_NAME = "DSApi";
        private  String TOKENS_DIRECTORY_PATH = "tokens_drive";
        private  String CREDS_STORE = "/credstore/creds.json";
        private  List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
        private boolean IS_SERVICE_ACCOUNT = true;

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

        /**
         *
         * @param bool Sets which authorisation flow to use Default is service account flow
         * @return Builder Object
         */
        public Builder<T> IS_SERVICE_ACCOUNT(boolean bool) {
            this.IS_SERVICE_ACCOUNT = bool;
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