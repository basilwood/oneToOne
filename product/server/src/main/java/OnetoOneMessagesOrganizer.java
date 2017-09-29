import javax.json.JsonObject;
import javax.ws.rs.container.AsyncResponse;
import java.util.HashMap;
import java.util.PriorityQueue;

class OnetoOneMessagesOrganizer {

    HashMap <String, PriorityQueue> asyncMap = new HashMap <String, PriorityQueue>();
    HashMap <String, PriorityQueue> messageRouter = new HashMap <String, PriorityQueue>();

    public String putMessageInTheQueue( JsonObject message ) {
        String from = message.getString( "from" );
        String typedText = message.getString( "typedText" );

        if (messageRouter.containsKey( from )) {
            if (asyncMap.containsKey( from )) {
                PriorityQueue async = asyncMap.get( from );
                if (async.isEmpty()) {
                    PriorityQueue messageQueue = messageRouter.get( from );
                    messageQueue.add( typedText );
                } else {
                    AsyncResponse asyncResponse = (AsyncResponse) async.poll();
                    asyncResponse.resume( typedText );
                }
            } else {
                PriorityQueue messageQueue = messageRouter.get( from );
                messageQueue.add( typedText );
            }
            return "message sent";
        } else {
            if (asyncMap.containsKey( from )) {
                PriorityQueue async = asyncMap.get( from );
                if (async.isEmpty()) {
                    PriorityQueue <String> clientMessage = new PriorityQueue <String>();
                    clientMessage.add( typedText );
                    messageRouter.put( from, clientMessage );
                } else {
                    AsyncResponse asyncResponse = (AsyncResponse) async.poll();
                    asyncResponse.resume( typedText );
                }
            } else {
                PriorityQueue <String> clientMessage = new PriorityQueue <String>();
                clientMessage.add( typedText );
                messageRouter.put( from, clientMessage );
            }
            return "message sent";
        }
    }

    public void getMessagesFromTheQueue( AsyncResponse async, JsonObject message ) {
        String to = message.getString( "to" );
        if (messageRouter.containsKey( to )) {
            PriorityQueue messageFromQueue = messageRouter.get( to );
            if (messageFromQueue.isEmpty()) {
                PriorityQueue <AsyncResponse> asyncQueue = new PriorityQueue <AsyncResponse>();
                asyncQueue.add( async );
                asyncMap.put( to, asyncQueue );
            } else {
                String typedText = (String) messageFromQueue.poll();
                processAsync( async, typedText );
            }
        } else {
            PriorityQueue <AsyncResponse> asyncQueue = new PriorityQueue <AsyncResponse>();
            asyncQueue.add( async );
            asyncMap.put( to,asyncQueue );
        }
    }

    public void processAsync(AsyncResponse async,String replyMessage) {

        async.resume( replyMessage );
    }
}