package com.onetoone.store;

import com.onetoone.service.Authenticator;
import com.onetoone.service.ShareLinkHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.MalformedURLException;

public class UserManager {
    private static final String PERSISTENCE_UNIT_NAME = "users";
    private static EntityManagerFactory factory;
    private static EntityManager em;

    public void signUp( String name, String email) {

        factory = Persistence.createEntityManagerFactory("user");
        em = factory.createEntityManager();
        em.getTransaction().begin();

        User user = em.find( User.class,email );

        if (user == null) {
            User newUser = new User();
            try {
                newUser.setName( name );
                newUser.setEmail( email );
                em.persist( newUser );
                em.getTransaction().commit();
            } catch (Exception e) {
                throw e;
            }
            finally {
                em.close();
                factory.close();
            }
        }
    }

    public void addShareCodeToTheUser(String base64encodedString, String email, String code){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("user");
        EntityManager em = factory.createEntityManager();
        try {
            User user = em.find( User.class,email );
            em.getTransaction().begin();

            user.setSharedCode( code );

            em.persist( user );
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
            factory.close();
        }
        //return base64encodedString;
    }

    public void addPartnerIdToTheUser( String encryptedId) throws MalformedURLException {
        ShareLinkHandler decodeCode = new ShareLinkHandler();
        String decodedCode = decodeCode.decodeWithBase64( encryptedId );
        String decodedEmail = decodedCode.substring( 0, decodedCode.length() - 4 );
        String decodedUniqueId = decodedCode.substring( decodedCode.length() - 4 );

        factory = Persistence.createEntityManagerFactory("user");
        em = factory.createEntityManager();
        em.getTransaction().begin();

        try {
            User user = em.find( User.class, decodedEmail );
            if (user.getSharedCode().equals( decodedUniqueId )) {
                user.setPartneremail( Authenticator.email );

                em.persist( user );
                em.getTransaction().commit();
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            em.close();
            factory.close();
        }
    }
}