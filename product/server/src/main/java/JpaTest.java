import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class JpaTest {
    private static final String PERSISTENCE_UNIT_NAME = "todos";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        /*factory = Persistence.createEntityManagerFactory("todos");
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
//        Query q = em.createQuery("select t from Todo t");
//        List<Todo> todoList = q.getResultList();
//        for (Todo todo : todoList) {
//            System.out.println(todo);
//        }
        //System.out.println("Size: " + todoList.size());
//
//        String ddl = "CREATE TABLE Todo ( Summary varchar(255), description varchar(255))";
//        em.createNativeQuery(ddl);

        // create new todo
        em.getTransaction().begin();
        Todo todo = new Todo();
        todo.setSummary("This is a test");
        todo.setDescription("This is a test");
        em.persist(todo);
        em.getTransaction().commit();

        em.close();
    } */
        System.out.println( " hello from jpa" );
    }
    public void returnSomething(String name, String email, String password) {

        factory = Persistence.createEntityManagerFactory("todos");
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Todo todo = new Todo();
        todo.setName( name );
        todo.setEmail( email );
        todo.setPassword(password);
        em.persist(todo);
        em.getTransaction().commit();

        em.close();
        factory.close();

        //Todo todo = new Todo();
        //return todo.getName();

    }
}