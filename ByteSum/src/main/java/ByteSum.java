import java.util.Arrays;
import java.util.Random;

public class ByteSum {

  public static void main(String[] args) {
    final Random rnd = new Random();

    byte[] values = new byte[32768];
    rnd.nextBytes(values);

    long start = System.nanoTime();
    long sum = 0;

    for (int repetition = 0; repetition < 100_000; repetition++) {
      for (int i = 0; i < values.length; i++) {
        if (values[i] >= 0) {
          sum += values[i];
        }
      }
    }

    double duration = (System.nanoTime() - start) / 1000.0 / 1000.0;
    System.out.println(String.format("duration = %.1fms", duration));
  }
}
