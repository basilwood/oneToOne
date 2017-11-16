/**
 * Created by ajuvignesh on 16/09/17.
 */

/** First receive has to be called and then send */

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


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
    public String authenticate(JsonObject message) throws MalformedURLException, BadJOSEException, ParseException, JOSEException {
        Logger logger = Logger.getLogger( getClass().getName() );
        logger.info( "abc123");
        Authentication authentication = new Authentication();
        authentication.authenticate( message );
        //JpaTest jpaTest = new JpaTest();
        //String returnValue = jpaTest.returnSomething();
        //return "The auth message is "+ authMessage + "The return from database is "+ returnValue;
        return "authenticated and saved in db";
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

    @Path( "/get" )
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testMessage(){
        return "Hello from onetoone";
    }
}
