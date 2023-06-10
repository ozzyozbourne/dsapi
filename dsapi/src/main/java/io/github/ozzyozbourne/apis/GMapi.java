package io.github.ozzyozbourne.apis;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author osaid khan
 * @version 5.1.0
 * @param <T> Class containing the necessary credentials files
 * A CRUD wrapper for Gmail Api that uses Objects as string for all CRUD operations
 */
public class GMapi <T> {
    private final String APPLICATION_NAME;
    private final String TOKENS_DIRECTORY_PATH;
    private final String CREDS_STORE;
    private final JsonFactory JSON_FACTORY;
    private final List<String> SCOPES;
    private final NetHttpTransport netHttpTransport;
    /**
     * Exposing the Gmail obj so users can build their own custom functions from it
     */
    public  final Gmail gmailService;
    private final  Class<T> RESOURCE_CLASS;

    private GMapi(GMapi.Builder<T> builder)  {
        this.APPLICATION_NAME = builder.APPLICATION_NAME;
        this.RESOURCE_CLASS = builder.RESOURCE_CLASS;
        this.TOKENS_DIRECTORY_PATH = builder.TOKENS_DIRECTORY_PATH;
        this.CREDS_STORE = builder.CREDS_STORE;
        this.SCOPES = builder.SCOPES;
        this.JSON_FACTORY = GsonFactory.getDefaultInstance();
        this.netHttpTransport = CommonAuth.getNetHttpTransPort();
        this.gmailService = getDSAuth();
    }

    private Gmail getDSAuth(){
        return new Gmail.Builder(netHttpTransport, JSON_FACTORY,
                CommonAuth.authorize(RESOURCE_CLASS, CREDS_STORE, JSON_FACTORY, netHttpTransport, SCOPES, TOKENS_DIRECTORY_PATH))
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     *
     * @param <T> Class containing the necessary credentials files
     */
    public static class Builder<T>{

        private final Class<T> RESOURCE_CLASS;
        private  String APPLICATION_NAME = "DSApi";
        private  String TOKENS_DIRECTORY_PATH = "tokens_gmail";
        private  String CREDS_STORE = "/credstore/auth.json";
        private  List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);

        /**
         *
         * @param t Variable of the class of type T
         * @return Buider Object
         * @param <T> Class containing the necessary credentials files
         */
        public static <T> GMapi.Builder<T> getBuilder(Class<T> t){
            return new GMapi.Builder<>(t);
        }

        /**
         *
         * @return the Configured Google Gmail object
         */
        public GMapi<T> build()
        {
            return new GMapi<>(this);
        }

        private Builder(Class<T> RESOURCE_CLASS ) {
            this.RESOURCE_CLASS = RESOURCE_CLASS;
        }

        /**
         *
         * @param APPLICATION_NAME Sets the application name default name is DSApi
         * @return Builder Object
         */
        public GMapi.Builder<T> setAPPLICATION_NAME(String APPLICATION_NAME) {
            this.APPLICATION_NAME = APPLICATION_NAME;
            return this;
        }

        /**
         *
         * @param TOKENS_DIRECTORY_PATH sets the token directory path DEFAULT is tokens
         * @return Builder Object
         */
        public GMapi.Builder<T> setTOKENS_DIRECTORY_PATH(String TOKENS_DIRECTORY_PATH) {
            this.TOKENS_DIRECTORY_PATH = TOKENS_DIRECTORY_PATH;
            return this;
        }

        /**
         *
         * @param CREDS_STORE sets the path to Google gmail credentials in the resource folder
         * @return Builder Object
         */
        public GMapi.Builder<T> setCREDS_STORE(String CREDS_STORE) {
            this.CREDS_STORE = CREDS_STORE;
            return this;
        }

        /**
         *
         * @param SCOPES Sets the scope of gmail default is read and write both
         * @return Builder Object
         */
        public GMapi.Builder<T> setSCOPES(List<String> SCOPES) {
            this.SCOPES = SCOPES;
            return this;
        }

    }

    @Override
    public String toString() {
        return "GMapi{" +
                "APPLICATION_NAME='" + APPLICATION_NAME + '\'' +
                ", TOKENS_DIRECTORY_PATH='" + TOKENS_DIRECTORY_PATH + '\'' +
                ", CREDS_STORE='" + CREDS_STORE + '\'' +
                ", JSON_FACTORY=" + JSON_FACTORY +
                ", SCOPES=" + SCOPES +
                ", netHttpTransport=" + netHttpTransport +
                ", gmailService=" + gmailService +
                ", RESOURCE_CLASS=" + RESOURCE_CLASS +
                '}';
    }
}
