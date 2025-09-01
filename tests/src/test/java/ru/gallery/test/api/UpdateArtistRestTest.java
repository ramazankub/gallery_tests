package ru.gallery.test.api;

import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayRestClient;
import ru.gallery.service.AuthApiClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;

public class UpdateArtistRestTest {

    private final ArtistGatewayRestClient artistGatewayRestClient = new ArtistGatewayRestClient();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void updateArtistTest() {
        final String token = authApiClient.login("test", "12345");
        final String bearerToken = "Bearer " + token;

        ArtistJson createdArtistJson = artistGatewayRestClient.addArtist(bearerToken,
                new ArtistJson(
                        null,
                        randomArtistName(),
                        randomText(),
                        ""
        ));

        ArtistJson actualArtist = artistGatewayRestClient.updateArtist(bearerToken, new ArtistJson(
                createdArtistJson.id(),
                randomArtistName(),
                randomText(),
                ""
        ));

        assertEquals(createdArtistJson.id(), actualArtist.id());
        assertEquals(createdArtistJson.name(), actualArtist.name());
        assertEquals(createdArtistJson.biography(), actualArtist.biography());
        assertEquals(createdArtistJson.photo(), actualArtist.photo());
    }
}
