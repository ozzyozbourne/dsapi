package io.github.ozzyozbourne.apis;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author osaid khan
 * @version 4.2.0
 * @param <T> Class containing the necessary credentials files
 * A CRUD wrapper for Google Docs Api that uses Objects as string for all CRUD operations
 */
public class GDCapi<T> {
    private final String APPLICATION_NAME;
    private final String TOKENS_DIRECTORY_PATH;
    private final String CREDS_STORE;
    private final JsonFactory JSON_FACTORY;
    private final List<String> SCOPES;
    private final NetHttpTransport netHttpTransport;
    /**
     * Exposing the docs obj so users can build their own custom functions from it
     */
    public  final Docs docsService;
    private final  Class<T> RESOURCE_CLASS;

    private GDCapi(Builder<T> builder)  {
        this.APPLICATION_NAME = builder.APPLICATION_NAME;
        this.RESOURCE_CLASS = builder.RESOURCE_CLASS;
        this.TOKENS_DIRECTORY_PATH = builder.TOKENS_DIRECTORY_PATH;
        this.CREDS_STORE = builder.CREDS_STORE;
        this.SCOPES = builder.SCOPES;
        this.JSON_FACTORY = GsonFactory.getDefaultInstance();
        this.netHttpTransport = CommonAuth.getNetHttpTransPort();
        this.docsService = builder.IS_SERVICE_ACCOUNT?getDSService():getDSAuth();
    }

    private Docs getDSAuth(){
        return new  Docs.Builder(netHttpTransport, JSON_FACTORY,
                CommonAuth.authorize(RESOURCE_CLASS, CREDS_STORE, JSON_FACTORY, netHttpTransport, SCOPES, TOKENS_DIRECTORY_PATH))
                .setApplicationName(APPLICATION_NAME).build();
    }

    private Docs getDSService(){
        return new Docs.Builder(netHttpTransport, JSON_FACTORY,new HttpCredentialsAdapter
                (Objects.requireNonNull(CommonAuth.getGoogleCredentials(RESOURCE_CLASS, CREDS_STORE, SCOPES))))
                .setApplicationName(APPLICATION_NAME).build();
    }


    /**
     *
     * @param <T> Class containing the necessary credentials files
     */
    public static class Builder<T>{

        private final Class<T> RESOURCE_CLASS;
        private  String APPLICATION_NAME = "DSApi";
        private  String TOKENS_DIRECTORY_PATH = "tokens_docs";
        private  String CREDS_STORE = "/credstore/creds.json";
        private  List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
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
         * @return the Configured Google Docs object
         */
        public GDCapi<T> build()
        {
            return new GDCapi<>(this);
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
         * @param CREDS_STORE sets the path to Google docs credentials in the resource folder
         * @return Builder Object
         */
        public Builder<T> setCREDS_STORE(String CREDS_STORE) {
            this.CREDS_STORE = CREDS_STORE;
            return this;
        }

        /**
         *
         * @param SCOPES Sets the scope of docs default is read and write both
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

}