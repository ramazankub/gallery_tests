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

public class UpdateArtistTest {

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void updateArtistTest() {
        String username = randomUsername();
        authApiClient.createUser(username, DEFAULT_PASSWORD);
        final String token = authApiClient.login(username, DEFAULT_PASSWORD);
        ArtistJson addedArtist = new ArtistJson(
                null,
                randomArtistName(),
                randomText(),
                ""
        );
        UUID addedArtistId = artistGatewayClient.addArtist(token, addedArtist).id();

        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson updatedArtist = new ArtistJson(
                addedArtistId,
                randomArtistName(),
                randomText(),
                photo
        );
        ArtistJson actualArtistResponse = artistGatewayClient.updateArtist(token, updatedArtist);

        assertAll("Проверка полей художника, которого возвращает updateArtist",
                () -> assertEquals(updatedArtist.id(), actualArtistResponse.id()),
                () -> assertEquals(updatedArtist.name(), actualArtistResponse.name()),
                () -> assertEquals(updatedArtist.biography(), actualArtistResponse.biography()),
                () -> assertEquals(updatedArtist.photo(), actualArtistResponse.photo())
        );

        ArtistEntity actualArtistDb = artistRepository.findArtistById(addedArtistId);
        assertAll("Проверка полей художника из rococo-artist",
                () -> assertEquals(actualArtistResponse.id(), actualArtistDb.getId()),
                () -> assertEquals(updatedArtist.name(), actualArtistDb.getName()),
                () -> assertEquals(updatedArtist.biography(), actualArtistDb.getBiography())
        );
    }
}
