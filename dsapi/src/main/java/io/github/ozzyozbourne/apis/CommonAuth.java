package io.github.ozzyozbourne.apis;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

/**
 * @author osaid khan
 * @version 5.1.0
 * Common connection  Utilities
 */
final class CommonAuth {

    /**
     *
     * @return NetHttpTransport for communication
     */
   public static NetHttpTransport getNetHttpTransPort(){
        NetHttpTransport netHttpTransport;
        try {
            netHttpTransport =  GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return Objects.requireNonNull(netHttpTransport);
    }

    /**
     *
     * @param RESOURCE_CLASS Class containing the credentials in resource path for authentication
     * @param CREDS_STORE Location of the folder contains the creds.json
     * @param JSON_FACTORY GOOGLE JSON factory for communication
     * @param netHttpTransport NetHttpTransport for communication
     * @param SCOPES List of Scopes for the APIs
     * @param TOKENS_DIRECTORY_PATH location in disk to store the generated tokens
     * @return Credentials Object for authentication
     * @param <T> Variable of the class of type T
     */

   public static <T> Credential authorize(Class<T> RESOURCE_CLASS, String CREDS_STORE, JsonFactory JSON_FACTORY, NetHttpTransport netHttpTransport, List<String> SCOPES, String TOKENS_DIRECTORY_PATH){

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

    /**
     *
     * @param RESOURCE_CLASS Class containing the credentials in resource path for authentication
     * @param CREDS_STORE Location of the folder contains the creds.json
     * @param SCOPES List of Scopes for the APIs
     * @return GoogleCredentials for authentication
     * @param <T> Variable of the class of type T
     */

   public static <T> GoogleCredentials getGoogleCredentials(Class<T> RESOURCE_CLASS, String CREDS_STORE, List<String> SCOPES) {
       GoogleCredentials credentials;
       try {
           credentials = GoogleCredentials.fromStream(Objects.requireNonNull(RESOURCE_CLASS.getResourceAsStream(CREDS_STORE)));
           credentials.createScoped(SCOPES).refreshIfExpired();
       } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
       return credentials;
   }

}
