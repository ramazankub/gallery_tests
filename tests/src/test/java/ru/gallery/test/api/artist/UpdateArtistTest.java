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

public class UpdateArtistTest {

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
    void updateArtistTest() {
        ArtistJson addedArtist = new ArtistJson(
                null,
                randomArtistName(),
                randomText(),
                ""
        );
        // Сначала создаем художника
        UUID addedArtistId = artistGatewayClient.addArtist(token, addedArtist).id();

        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        // Готовим нового художника с новыми данными
        ArtistJson updatedArtist = new ArtistJson(
                addedArtistId,
                randomArtistName(),
                randomText(),
                photo
        );
        // Обновляем художника
        ArtistJson actualArtistResponse = artistGatewayClient.updateArtist(token, updatedArtist);

        // Проверяем что в ответе художник уже обновленный
        assertAll("Проверка полей художника, которого возвращает updateArtist",
                () -> assertEquals(updatedArtist.id(), actualArtistResponse.id()),
                () -> assertEquals(updatedArtist.name(), actualArtistResponse.name()),
                () -> assertEquals(updatedArtist.biography(), actualArtistResponse.biography()),
                () -> assertEquals(updatedArtist.photo(), actualArtistResponse.photo())
        );

        // Получаем художника из базы по id
        ArtistEntity actualArtistDb = artistRepository.findArtistById(addedArtistId);
        // Проверяем что художник в ответе, такой же как и в базе
        assertAll("Проверка полей художника из rococo-artist",
                () -> assertEquals(actualArtistResponse.id(), actualArtistDb.getId()),
                () -> assertEquals(updatedArtist.name(), actualArtistDb.getName()),
                () -> assertEquals(updatedArtist.biography(), actualArtistDb.getBiography())
        );
    }
}
