import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class VoucherCreator {
  private static final Logger LOGGER = LoggerFactory.getLogger(VoucherCreator.class);
  private EntityManager em;

  public VoucherCreator(EntityManager em) {
    this.em = em;
  }


  public void createVouchers(Long quantity) {
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


  private String findUnusedCode() {
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




  public void assertVoucherCount(long expected) {
    em.getTransaction().begin();
    TypedQuery<Long> q = em.createQuery("select count(*) from Voucher", Long.class);
    Long quantity = q.getSingleResult();
    assertThat(quantity).isEqualTo(expected);
    em.getTransaction().commit();
  }
}
