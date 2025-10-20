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

public class GetArtistRestTest {

    private final ArtistGatewayRestClient artistGatewayRestClient = new ArtistGatewayRestClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void getArtistTest() {
        final String token = authApiClient.login("test", "12345");
        ArtistJson createdArtistJson = artistGatewayRestClient.addArtist("Bearer " + token,
                new ArtistJson(
                        null,
                        randomArtistName(),
                        randomText(),
                        "123"
                ));
        String createdArtistId = createdArtistJson.id().toString();

        ArtistJson actualArtist = artistGatewayRestClient.getArtist(createdArtistId);

        ArtistEntity expectedArtist = artistRepository.findArtistById(UUID.fromString(createdArtistId));
        assertEquals(expectedArtist.getId(), actualArtist.id());
    }
}
