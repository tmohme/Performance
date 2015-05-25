import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum PersistenceManager {
  INSTANCE;

  final private EntityManagerFactory emFactory;

  PersistenceManager() {
    emFactory = Persistence.createEntityManagerFactory("jpa-example");
  }

  public EntityManager getEntityManager() {
    return emFactory.createEntityManager();
  }

  public void close() {
    emFactory.close();
  }
}