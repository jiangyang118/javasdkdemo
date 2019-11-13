package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CountDownLatch;

/**
 * run this main method and check result
 */
public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String args[]) throws Exception {
		FabricHelper helper = FabricHelper.getInstance();
		helper.setConfigCtx("D:/code/huawei/blockchain/javasdkdemo/config/bcs-plkt4x-fabcar-sdk-config.yaml");
		LoopInvoke(1);
		// StartMultiTask(1,1);
//		invokeFabcar();
	}

	public static void LoopInvoke(int loop) throws Exception {
		FabricHelper helper = FabricHelper.getInstance();
		for (int i = 0; i < loop; i++) {
			helper.invokeBlockchain("invoke", new String[] { "b", "a", "500" });
			String a = helper.queryBlockchain("query", new String[] { "a" });
			String b = helper.queryBlockchain("query", new String[] { "b" });
			logger.info("after invoke  a=" + a + ", invoke b=" + b);
		}
	}

	// StartMultiTask(1, 1);
	public static void StartMultiTask(int threadNumber, int loop) throws InterruptedException {
		final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
		for (int i = 0; i < threadNumber; i++) {
			final int threadID = i;
			new Thread() {
				public void run() {
					try {
						LoopInvoke(loop);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.info("threadID:[%s] finished!!", threadID);
					countDownLatch.countDown();
				}
			}.start();
		}

		countDownLatch.await();
		logger.info("main thread finished!!");
	}

	public static void invokeFabcar() throws Exception {
		FabricHelper helper = FabricHelper.getInstance();
//		helper.invokeBlockchain("initLedger", new String[] {});
		String res = helper.queryBlockchain("queryAllCars", new String[] {});
		logger.info("after invoke  res=" + res);
		helper.invokeBlockchain("changeCarOwner", new String[] { "CAR0", "JACKIAL" });

		helper.invokeBlockchain("createCar", new String[] { "CAR10", "AAAAA", "BBBBB", "CCCCC", "DDDDDD" });
		res = helper.queryBlockchain("queryCar", new String[] { "CAR0" });
		logger.info("CAR0=" + res);

		res = helper.queryBlockchain("queryCar", new String[] { "CAR10" });
		logger.info("CAR10=" + res);
	}

}
