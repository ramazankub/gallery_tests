package ru.gallery.test.api.artist;

import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAllArtistTest {

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    @Test
    void getAllArtistTest() {
        final int defaultArtistCount = 10;

        List<ArtistJson> artistJsonList = artistGatewayClient.getAllArtists();

        List<ArtistEntity> artistEntityList = artistRepository.findAllArtists(defaultArtistCount);
        assertEquals(artistJsonList.size(), artistEntityList.size());
    }
}
