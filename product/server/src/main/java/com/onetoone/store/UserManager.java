package com.onetoone.store;

import com.onetoone.service.Authenticator;
import com.onetoone.service.ShareLinkHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.MalformedURLException;
import java.util.logging.Logger;


public class UserManager {
    private static final String PERSISTENCE_UNIT_NAME = "users";
    private static EntityManagerFactory factory;
    private static EntityManager em;

    public void signUp( String name, String email) {

        Logger logger = Logger.getLogger( getClass().getName() );
        logger.info( String.format("signup initiated"));

        factory = Persistence.createEntityManagerFactory("user");
        em = factory.createEntityManager();
        em.getTransaction().begin();

        logger = Logger.getLogger( getClass().getName() );
        logger.info( String.format("before em.find"));

        User user = em.find( User.class,email );


        logger = Logger.getLogger( getClass().getName() );
        logger.info( String.format("after em.find"));

        if (user == null) {

            logger = Logger.getLogger( getClass().getName() );
            logger.info( String.format("before user object"));

            User newUser = new User();

            logger = Logger.getLogger( getClass().getName() );
            logger.info( String.format("after user object"));

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

    public String addPartnerIdToTheUser( String encryptedId) throws MalformedURLException {
        ShareLinkHandler decodeCode = new ShareLinkHandler();
        String decodedCode = decodeCode.decodeWithBase64( encryptedId );
        String decodedEmail = decodedCode.substring( 0, decodedCode.length() - 4 );
        String decodedUniqueId = decodedCode.substring( decodedCode.length() - 4 );

        factory = Persistence.createEntityManagerFactory("user");
        em = factory.createEntityManager();
        em.getTransaction().begin();

        try {
            User user = em.find( User.class, decodedEmail );
            if (Authenticator.email.equals( decodedEmail )){
                Logger logger = Logger.getLogger( getClass().getName() );
                logger.info( String.format("Auth email equals decoded email"));

            } else if (user.getSharedCode().equals( decodedUniqueId )) {
                Logger logger = Logger.getLogger( getClass().getName() );
                logger.info( String.format("inside add partnerIDTOTHEUSER else if ***"));
                user.setPartneremail( Authenticator.email );

                user = em.find( User.class,Authenticator.email );
                user.setPartneremail( decodedEmail );

                String uniqueCoupleID = Authenticator.email.concat( decodedEmail );

                user.setUniqueCoupleId(uniqueCoupleID);

                user = em.find( User.class, decodedEmail );
                user.setUniqueCoupleId( uniqueCoupleID );

                em.persist( user );
                em.getTransaction().commit();
            }
            Logger logger = Logger.getLogger( getClass().getName() );
            logger.info( String.format("before returning decoded mail"));
            return decodedEmail;
        }

//        try {
//            User user = em.find( User.class, decodedEmail );
//            if (user.getSharedCode().equals( decodedUniqueId )) {
//                user.setPartneremail( Authenticator.email );
//
//                em.persist( user );
//                em.getTransaction().commit();
//            }
//        }
        catch (Exception e) {
            throw e;
        }
        finally {
            em.close();
            factory.close();
        }
    }

    public String findUniqueId(String mailId) {
        factory = Persistence.createEntityManagerFactory("user");
        em = factory.createEntityManager();
        em.getTransaction().begin();

        try {
            User user = em.find( User.class, mailId );
                String uniqueId = user.getUniqueCoupleId();
                em.persist( user );
                em.getTransaction().commit();
                return uniqueId;
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