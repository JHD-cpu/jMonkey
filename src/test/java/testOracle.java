import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class testOracle {

  public static void main(String[] args) {

    ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<Runnable>(50);
    ThreadPoolExecutor threadPoolExecutor =
        new ThreadPoolExecutor(
            5,
            30,
            30,
            TimeUnit.SECONDS,
            arrayBlockingQueue,
            new ThreadFactory() {
              @Override
              public Thread newThread(Runnable r) {
                return new Thread(r, r.hashCode() + "");
              }
            });
    for (int i = 0; i < 30; i++) {
      threadPoolExecutor.submit(
          new Runnable() {
            @Override
            public void run() {
              JDBCUtil.query("select * from user_tables");
            }
          });
    }
  }
}
