package personal.hdproject.util.encryption;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EncryptedSourceDto {

	private final String encryptedSource;
	private final String salt;

	@Builder
	private EncryptedSourceDto(String encryptedSource, String salt) {
		this.encryptedSource = encryptedSource;
		this.salt = salt;
	}

	public static EncryptedSourceDto toEncryptedPasswordDto(String encryptedSource, String salt) {
		return EncryptedSourceDto.builder()
			.encryptedSource(encryptedSource)
			.salt(salt)
			.build();
	}
}
