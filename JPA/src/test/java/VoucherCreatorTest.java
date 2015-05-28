import org.h2.tools.Server;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.sql.SQLException;

public class VoucherCreatorTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(VoucherCreatorTest.class);
  private static Server server;

  private EntityManager em;

  @BeforeClass
  public static void beforeClass() throws SQLException {
    server = Server.createTcpServer("-tcpDaemon").start();
  }

  @AfterClass
  public static void afterClass() {
    if (server != null) {
      server.stop();
    }
  }

  @Before
  public void before() {
    em = PersistenceManager.INSTANCE.getEntityManager();
  }

  @After
  public void after() {
    em.close();
    PersistenceManager.INSTANCE.close();
  }

  @Test
  public void createVouchers(){
    final long voucherCount = 1_000;

    VoucherCreator vc = new VoucherCreator(em);
    vc.createVouchers(voucherCount);
    vc.assertVoucherCount(voucherCount);
  }

}
