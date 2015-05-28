import javax.persistence.EntityManager;

class Main {
  private static final long NUM_VOUCHERS = 1_000;

  private static EntityManager em;


  public static void main(String[] args) {
    em = PersistenceManager.INSTANCE.getEntityManager();

    VoucherCreator vc = new VoucherCreator(em);
    vc.createVouchers(NUM_VOUCHERS);
    vc.assertVoucherCount(NUM_VOUCHERS);

    em.close();
    PersistenceManager.INSTANCE.close();
  }

}