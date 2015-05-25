import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private static final long NUM_VOUCHERS = 1_000;

  private static EntityManager em;


  public static void main(String[] args) {
    em = PersistenceManager.INSTANCE.getEntityManager();

    createVouchers(NUM_VOUCHERS);
    assertVoucherCount(NUM_VOUCHERS);

    em.close();
    PersistenceManager.INSTANCE.close();
  }


  private static void createVouchers(Long quantity) {
    LOGGER.info("about to insert {} vouchers", quantity);
    final long start = System.nanoTime();
    em.getTransaction().begin();


    for (int i=0; i<quantity; i++) {
      Voucher voucher = new Voucher(findUnusedCode());
      em.persist(voucher);
    }


    em.getTransaction().commit();
    final Duration duration = Duration.ofNanos(System.nanoTime() - start);
    LOGGER.info("insert duration = {}", duration);
    LOGGER.info("inserted {} vouchers/s", (1000 * quantity) / duration.toMillis());
    LOGGER.info("each voucher took {}", duration.dividedBy(quantity));
  }


  private static String findUnusedCode() {
    String code;

    List<Voucher> list;
    do {
      TypedQuery<Voucher> q = em.createNamedQuery(Voucher.VOUCHER_BY_CODE, Voucher.class);
      code = createUniqueCode();
      q.setParameter("code", code);
      list = q.getResultList();
    } while (!list.isEmpty());

    return code;
  }


  private static String createUniqueCode() {
    return UUID.randomUUID().toString().substring(0, 6);
  }



  private static void assertVoucherCount(long expected) {
    em.getTransaction().begin();
    TypedQuery<Long> q = em.createQuery("select count(*) from Voucher", Long.class);
    Long quantity = q.getSingleResult();
    assertThat(quantity).isEqualTo(expected);
    em.getTransaction().commit();
  }

}