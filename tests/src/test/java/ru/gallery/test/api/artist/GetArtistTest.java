package ru.gallery.test.api.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayClient;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;
import static ru.gallery.utils.DataUtils.randomUsername;

public class GetArtistTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private String token;

    @BeforeEach
    void createUserAndLogin() {
        String username = randomUsername();
        authApiClient.createUser(username, DEFAULT_PASSWORD);
        token = authApiClient.login(username, DEFAULT_PASSWORD);
    }

    @Test
    void getArtistTest() {
        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson expectedArtist = ArtistJson.builder()
                                              .name(randomArtistName())
                                              .biography(randomText())
                                              .photo(photo)
                                              .build();

        // Чтобы получить художника, нужно сначала его положить в базу. Кладем, и получаем его id
        UUID addedArtistId = artistGatewayClient.addArtist(token, expectedArtist).id();
        // Отправляем запрос на получение художника по id
        ArtistJson actualArtistResponse = artistGatewayClient.getArtist(addedArtistId.toString());

        // Проверяем что полученный художник такой же, как и созданный
        assertAll("Проверка полей художника, которого возвращает getArtist",
                () -> assertEquals(expectedArtist.name(), actualArtistResponse.name()),
                () -> assertEquals(expectedArtist.biography(), actualArtistResponse.biography()),
                () -> assertEquals(expectedArtist.photo(), actualArtistResponse.photo())
        );

        // Получаем художника из базы
        ArtistEntity actualArtistDb = artistRepository.findArtistById(addedArtistId);
        // Проверяем что художник в базе такой же как и в ответе
        assertAll("Проверка полей художника из rococo-artist",
                () -> assertEquals(actualArtistResponse.id(), actualArtistDb.getId()),
                () -> assertEquals(expectedArtist.name(), actualArtistDb.getName()),
                () -> assertEquals(expectedArtist.biography(), actualArtistDb.getBiography())
        );
    }
}
