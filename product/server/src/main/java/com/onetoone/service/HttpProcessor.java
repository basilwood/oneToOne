package com.onetoone.service; /**
 * Created by ajuvignesh on 16/09/17.
 */

/** First receive has to be called and then send */

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.onetoone.store.UserManager;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/otoo")
public class HttpProcessor {
    //static String id;
    static String id2;
    static int messageNumber = 1;
    static String queueMessage;
    static int counter = 1;


    public static HashMap <String, Object> userPairMap = new HashMap <String, Object>();

    @Path( "/authenticate" )
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes (MediaType.APPLICATION_JSON)
    public Response authenticate( JsonObject message) throws MalformedURLException, BadJOSEException, ParseException, JOSEException {
        Logger logger = Logger.getLogger( getClass().getName() );
        logger.info( "abc123");
        Authenticator Authenticator = new Authenticator();
        try {
            logger = Logger.getLogger( getClass().getName() );
            logger.info( "before authenticate");
            Authenticator.authenticate( message );
            return Response.ok().build();
        } catch (Exception e){
            logger = Logger.getLogger( getClass().getName() );
            logger.info( "exception" + e);
            return Response.status( Response.Status.FORBIDDEN ).entity( "Message from authentication" ).build();
        }
    }


    @Path("/send")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String receiveMessageFromClient( JsonObject message ) throws InterruptedException {
        String id = message.getString( "id" );
        //String chatMessage = message.getString( "typedText" );

        if (userPairMap.containsKey( id )){
            OnetoOneMessagesOrganizer onetoOneMessagesOrganizer = (OnetoOneMessagesOrganizer) userPairMap.get( id );
            String reply = onetoOneMessagesOrganizer.putMessageInTheQueue( message );
            return reply;
        } else {
            OnetoOneMessagesOrganizer onetoOneMessagesOrganizer = new OnetoOneMessagesOrganizer();
            userPairMap.put( id, onetoOneMessagesOrganizer );
            String reply = onetoOneMessagesOrganizer.putMessageInTheQueue( message );
            return reply;
        }

    }

    @Path("/receive")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendMessageToTheClient(@Suspended AsyncResponse async, JsonObject message ) throws InterruptedException {
        String id = message.getString( "id" );

        if (userPairMap.containsKey( id )){
            OnetoOneMessagesOrganizer onetoOneMessagesOrganizer = (OnetoOneMessagesOrganizer) userPairMap.get( id );
            onetoOneMessagesOrganizer.getMessagesFromTheQueue(async, message);
        } else {
            OnetoOneMessagesOrganizer onetoOneMessagesOrganizer = new OnetoOneMessagesOrganizer();
            userPairMap.put( id, onetoOneMessagesOrganizer );
            onetoOneMessagesOrganizer.getMessagesFromTheQueue(async, message);
        }
    }

    @Path( "/share" )
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response ShareTheLink( String email){
        Logger logger = Logger.getLogger( getClass().getName() );
        logger.info( "the json is "+email );
        ShareLinkHandler shareLinkHandler = new ShareLinkHandler();
        try {
            String link = shareLinkHandler.encodeWithBase64(email);
            Logger logger1 = Logger.getLogger( getClass().getName() );
            //logger1.info( "The sharelink is "+link );
            logger1.info( String.format( "The sharelink is %1s",link ) );
            return Response.ok( link ).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity( "Share error handling" ).build();
        }
    }

    @Path( "/validate" )
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response verifyThePartner( String encryptedCode) throws MalformedURLException {
        UserManager userManager = new UserManager();
        try {
            String returnMailId = userManager.addPartnerIdToTheUser( encryptedCode );
            return Response.status(Response.Status.OK).entity( "validated" + returnMailId ).build();
        }
        catch (Exception e){
            return Response.status( Response.Status.FORBIDDEN ).entity( "The error is from validate" ).build();
        }
    }

    @Path( "/get" )
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testMessage(){
        return "Hello from onetoone";
    }

    @Path( "/findid" )
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response findUniqueId( String mailId) throws MalformedURLException {
        try {
            UserManager userManager = new UserManager();
            String uniqueId = userManager.findUniqueId( mailId );
            return Response.status(Response.Status.OK).entity( uniqueId ).build();
        }
        catch (Exception e){
            return Response.status( Response.Status.FORBIDDEN ).entity( "The error is from findUniqueId" ).build();
        }
    }
}
