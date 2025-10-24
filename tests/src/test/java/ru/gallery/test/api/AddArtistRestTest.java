package ru.gallery.test.api;

import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayRestClient;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.DEFAULT_USERNAME;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;

public class AddArtistRestTest {

    private final ArtistGatewayRestClient artistGatewayRestClient = new ArtistGatewayRestClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void addArtistTest() {
        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson artistJson = new ArtistJson(
                null,
                randomArtistName(),
                randomText(),
                photo
        );

        final String token = authApiClient.login(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        ArtistJson artistJsonResponse = artistGatewayRestClient.addArtist(token, artistJson);

        assertSoftly(softly -> {
                    assertEquals(artistJson.name(), artistJsonResponse.name());
                    assertEquals(artistJson.biography(), artistJsonResponse.biography());
                    assertEquals(artistJson.photo(), artistJsonResponse.photo());
                }
        );

        ArtistEntity actualArtist = artistRepository.findArtistById(artistJsonResponse.id());

        assertSoftly(softly -> {
                    assertEquals(artistJsonResponse.id(), actualArtist.getId());
                    assertEquals(artistJson.name(), actualArtist.getName());
                    assertEquals(artistJson.biography(), actualArtist.getBiography());
                }
        );
    }
}
