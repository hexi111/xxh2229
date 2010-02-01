import edu.rit.crypto.blockcipher.AES256Cipher;
import edu.rit.util.Hex;
import edu.rit.numeric.Statistics;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.LongForLoop;
import edu.rit.pj.reduction.SharedLongArray;
import edu.rit.pj.Comm;

public class AesTestSmp {
	static byte[] key;
	static long block = 0;
	static SharedLongArray stats;

	public static void main(String argv[]) throws Exception {
		int n = argv.length;
		stats = new SharedLongArray(128);

		// Start timing.
		long t1 = System.currentTimeMillis();
		Comm.init(argv);

		if (n != 2) {
			System.err.println("The number of the parameters is incorrect.");
			usage();
		}

		int length = argv[0].length();
		if (length != 64) {
			System.err
					.println("The first parameter should be a 64-hexadecimal-digit number.");
			usage();
		}

		key = Hex.toByteArray(argv[0]);

		try {
			block = Long.valueOf(argv[1]).longValue();
			if (block <= 1) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err
					.println("The second parameter should be a decimal number of type long(>1).");
			usage();
		}
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, block - 1, new LongForLoop() {
					AES256Cipher cipher;
					byte[] plainText = new byte[16];
					byte[] cipherText = new byte[16];
					long[] thr_stats = new long[128];

					public void start() {
						cipher = new AES256Cipher(key);
					}

					public void run(long first, long last) {
						for (long i = first; i <= last; i++) {

							plainText[15] = (byte) (i);
							plainText[14] = (byte) (i >> 8);
							plainText[13] = (byte) (i >> 16);
							plainText[12] = (byte) (i >> 24);
							plainText[11] = (byte) (i >> 32);
							plainText[10] = (byte) (i >> 40);
							plainText[9] = (byte) (i >> 48);
							plainText[8] = (byte) (i >> 56);
							cipher.encrypt(plainText, cipherText);

							for (int k = 0; k < 8; k++) {

								thr_stats[k] += (cipherText[15] >> k & 0x01);
								thr_stats[8 + k] += (cipherText[14] >> k & 0x01);
								thr_stats[16 + k] += (cipherText[13] >> k & 0x01);
								thr_stats[24 + k] += (cipherText[12] >> k & 0x01);
								thr_stats[32 + k] += (cipherText[11] >> k & 0x01);
								thr_stats[40 + k] += (cipherText[10] >> k & 0x01);
								thr_stats[48 + k] += (cipherText[9] >> k & 0x01);
								thr_stats[56 + k] += (cipherText[8] >> k & 0x01);
								thr_stats[64 + k] += (cipherText[7] >> k & 0x01);
								thr_stats[72 + k] += (cipherText[6] >> k & 0x01);
								thr_stats[80 + k] += (cipherText[5] >> k & 0x01);
								thr_stats[88 + k] += (cipherText[4] >> k & 0x01);
								thr_stats[96 + k] += (cipherText[3] >> k & 0x01);
								thr_stats[104 + k] += (cipherText[2] >> k & 0x01);
								thr_stats[112 + k] += (cipherText[1] >> k & 0x01);
								thr_stats[120 + k] += (cipherText[0] >> k & 0x01);

							}
						}
					}

					public void finish() {
						for (int i = 0; i < 128; i++) {
							stats.addAndGet(i, thr_stats[i]);

						}
					}
				});
			}
		});

		double e0 = block / 2;
		for (int i = 0; i < 128; i++) {
			long m0 = block - stats.get(i);
			double chisqr = (stats.get(i) - e0) * (stats.get(i) - e0) / e0
					+ (m0 - e0) * (m0 - e0) / e0;
			double p = Statistics.chiSquarePvalue(1, chisqr);
			System.out.printf("%d\t%d\t%d\t%f\t%f%n", i, m0, stats.get(i),
					chisqr, p);
		}
		// Stop timing.
		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
	}

	private static void usage() {

		System.err.println("Usage: java AesTestSeq <key> <N>");
		System.err
				.println("<key> = Encryption key (a 64-hexadecimal-digit number)");
		System.err
				.println("<N> = The number of plaintext blocks (a decimal number of type long >=1)");
		System.exit(1);

	}
}
