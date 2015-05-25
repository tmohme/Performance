import java.util.Random;

public class Primes {
  private static final Random rnd = new Random();

  public static void main(String[] args) {
    final int numPrimes = 1_000_000;
    final int nanos_per_second = 1_000_000_000;
    Long current = 2L;
    Long count = 0L;
    Long lastPrime = 2L;

    long start = System.nanoTime();

    while (count < numPrimes) {

      if (isPrime(current)) {
        count++;
        lastPrime = current;
      }

      if (current == 2) {
        current++;
      } else {
        current = current + 2;
      }
    }

    System.out.println("Last prime = " + lastPrime);
    System.out.println("Total time = " + (double) (System.nanoTime() - start) / nanos_per_second);
  }

  private static Boolean isPrime(Long current) {
    final Long top = (long) Math.sqrt(current) + 1;

    for (Long i = 2L; i < top; i++) {
      if (current % i == 0) {
        return false;
      }
    }

    /*
    try {
      Thread.sleep(rnd.nextInt(100));
    } catch (InterruptedException e) {
      Thread.interrupted();
    }
    */

    return true;
  }
}