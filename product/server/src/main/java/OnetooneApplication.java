import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ajuvignesh on 05/09/17.
 */

@ApplicationPath("/app")
public class OnetooneApplication extends Application {
        @Override
        public Set <Class <?>> getClasses() {
            final Set <Class <?>> classes = new HashSet <Class <?>>();
            classes.add( HttpProcessor.class );
            return classes;
        }
    }
