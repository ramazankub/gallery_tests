package ru.gallery.test.api.artist;

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

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void getArtistTest() {
        String username = randomUsername();
        authApiClient.createUser(username, DEFAULT_PASSWORD);
        final String token = authApiClient.login(username, DEFAULT_PASSWORD);
        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson expectedArtist = new ArtistJson(
                null,
                randomArtistName(),
                randomText(),
                photo
        );

        UUID addedArtistId = artistGatewayClient.addArtist(token, expectedArtist).id();
        ArtistJson actualArtistResponse = artistGatewayClient.getArtist(addedArtistId.toString());

        assertAll("Проверка полей художника, которого возвращает getArtist",
                () -> assertEquals(expectedArtist.name(), actualArtistResponse.name()),
                () -> assertEquals(expectedArtist.biography(), actualArtistResponse.biography()),
                () -> assertEquals(expectedArtist.photo(), actualArtistResponse.photo())
        );

        ArtistEntity actualArtistDb = artistRepository.findArtistById(addedArtistId);
        assertAll("Проверка полей художника из rococo-artist",
                () -> assertEquals(actualArtistResponse.id(), actualArtistDb.getId()),
                () -> assertEquals(expectedArtist.name(), actualArtistDb.getName()),
                () -> assertEquals(expectedArtist.biography(), actualArtistDb.getBiography())
        );
    }
}
