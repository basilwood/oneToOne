import javax.json.JsonObject;
import javax.xml.registry.infomodel.EmailAddress;
import java.text.ParseException;

import java.net.*;
import java.util.logging.Logger;

import com.nimbusds.jose.*;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.nimbusds.openid.connect.sdk.validators.*;

public class Authentication {
    /*public String authenticate(JsonObject message) {
        String idToken = message.getString( "idToken" );
        Logger logger = Logger.getLogger( getClass().getName() );
        logger.info( "the token is" + idToken );
        return idToken;
    }*/
    public Authentication() throws MalformedURLException {
    }

        String name;
        String email;
        String picture;
        String password = "password";


        //JWT idtok = message.
        Issuer iss = new Issuer( "https://accounts.google.com" );
        ClientID clientID = new ClientID( "364222927183-g1ruj334rlj28268d32pm2ft30552s5h.apps.googleusercontent.com" );
        JWSAlgorithm jwsAlg = JWSAlgorithm.RS256;
        URL jwkSetURL = new URL( "https://www.googleapis.com/oauth2/v3/certs" );

        // Create validator for signed ID tokens
        IDTokenValidator validator = new IDTokenValidator( iss, clientID, jwsAlg, jwkSetURL );


    public void authenticate( JsonObject message ) throws MalformedURLException, ParseException, BadJOSEException, JOSEException {
        String idToken = message.getString( "idToken" );
        Logger logger = Logger.getLogger( getClass().getName() );
        logger.info( "The idetoken is "+ idToken);
        // Parse the ID token
        JWT idTok = JWTParser.parse( idToken );

        // Set the expected nonce, leave null if none
        //Nonce expectedNonce = new Nonce(); // or null

        IDTokenClaimsSet claims = validator.validate( idTok, null );
        //Subject subject = claims.getSubject();
        name = claims.getStringClaim("name");
        email = claims.getStringClaim( "email" );
        picture = claims.getStringClaim("picture");
//        Logger logger = Logger.getLogger( getClass().getName() );
//        logger.info( "The subjectString is "+ subjectString);
        JpaTest jpaTest = new JpaTest();
        jpaTest.returnSomething(name,email,password);

        //System.out.println( "Logged in user " + claims.getSubject() );
    }
}
