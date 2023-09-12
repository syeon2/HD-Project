package personal.hdproject.util.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sha256Util {
	private static final String ENCRYPTION_TYPE = "SHA-256";

	public static EncryptedSourceDto getEncryptDto(String source) {
		String salt = generateSalt();
		String encryptedSource = getEncryptedSourceWithSalt(source, salt);

		return EncryptedSourceDto.builder()
			.encryptedSource(encryptedSource)
			.salt(salt)
			.build();
	}

	public static String getEncryptedSourceWithSalt(String source, String salt) {
		try {
			MessageDigest md = MessageDigest.getInstance(ENCRYPTION_TYPE);

			byte[] byteData = md.digest(source.concat(salt).getBytes());

			return IntStream.range(0, byteData.length)
				.mapToObj(i -> String.format("%02x", byteData[i]))
				.collect(Collectors.joining());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 암호화 에러");
		}
	}

	private static String generateSalt() {
		ThreadLocalRandom random = ThreadLocalRandom.current();

		byte[] salt = new byte[8];
		random.nextBytes(salt);

		StringBuilder sb = new StringBuilder();
		for (byte b : salt) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
}
