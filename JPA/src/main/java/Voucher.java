import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "voucher")
@NamedQuery(name = Voucher.VOUCHER_BY_CODE, query = "from Voucher where code = :code")
public class Voucher {
  public static final String VOUCHER_BY_CODE = "VoucherByCode";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  private String veryImportant;

  private String alsoImportant;

  private String createdBy;
  private Timestamp createdAt;
  private String updatedBy;
  private Timestamp updatedAt;

  /* default constructor required by ORM */
  private Voucher() {}

  public Voucher(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "Voucher{" +
            "id=" + id +
            ", code='" + code + '\'' +
            '}';
  }
}