package ru.gallery.test.api;

import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayRestClient;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;

import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;

public class UpdateArtistRestTest {

    private final ArtistGatewayRestClient artistGatewayRestClient = new ArtistGatewayRestClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void updateArtistTest() {
        final String token = authApiClient.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        ArtistJson createdArtistJson = new ArtistJson(
                null,
                randomArtistName(),
                randomText(),
                ""
        );
        UUID addedArtistId = artistGatewayRestClient.addArtist(token, createdArtistJson).id();

        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson updatedArtistJson = new ArtistJson(
                addedArtistId,
                randomArtistName(),
                randomText(),
                photo
        );
        ArtistJson artistJsonResponse = artistGatewayRestClient.updateArtist(token, updatedArtistJson);

        assertSoftly(softly -> {
                    assertEquals(updatedArtistJson.id(), artistJsonResponse.id());
                    assertEquals(updatedArtistJson.name(), artistJsonResponse.name());
                    assertEquals(updatedArtistJson.biography(), artistJsonResponse.biography());
                    assertEquals(updatedArtistJson.photo(), artistJsonResponse.photo());
                }
        );

        ArtistEntity actualArtist = artistRepository.findArtistById(addedArtistId);
        assertSoftly(softly -> {
                    assertEquals(artistJsonResponse.id(), actualArtist.getId());
                    assertEquals(updatedArtistJson.name(), actualArtist.getName());
                    assertEquals(updatedArtistJson.biography(), actualArtist.getBiography());
                }
        );
    }
}
