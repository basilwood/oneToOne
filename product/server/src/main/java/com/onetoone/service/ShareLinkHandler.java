package com.onetoone.service;

import com.onetoone.store.User;
import com.onetoone.store.UserManager;
import org.glassfish.jersey.internal.util.Base64;

import java.security.SecureRandom;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import java.util.Base64;
/**
 * Created by ajuvignesh on 17/11/17.
 */
public class ShareLinkHandler {
    String code;

    public String appendEmailwithCode( String email) {
        SecureRandom random = new SecureRandom();
        code = String.format("%04d", random.nextInt(10000));
        return email+code;
    }

    public String encodeWithBase64( String email) {
        String base64encodedString = Base64.encodeAsString(appendEmailwithCode( email ));
        UserManager userManager = new UserManager();
        try {
            userManager.addShareCodeToTheUser( base64encodedString, email, code );
            return base64encodedString;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String decodeWithBase64(String encryptedCode) {
        return Base64.decodeAsString( encryptedCode );
    }
}
