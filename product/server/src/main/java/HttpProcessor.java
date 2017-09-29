/**
 * Created by ajuvignesh on 16/09/17.
 */

/** First receive has to be called and then send */

import java.util.*;

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
}
