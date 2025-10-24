package ru.gallery.test.api;

import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayRestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAllArtistsRestTest {

    private final ArtistGatewayRestClient artistGatewayRestClient = new ArtistGatewayRestClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    @Test
    void getAllArtistTest() {
        final int defaultArtistCount = 10;

        List<ArtistJson> artistJsonList = artistGatewayRestClient.getAllArtists();

        List<ArtistEntity> artistEntityList = artistRepository.findAllArtists(defaultArtistCount);
        assertEquals(artistJsonList.size(), artistEntityList.size());
    }
}
