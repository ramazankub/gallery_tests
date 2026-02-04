package ru.gallery.test.api.artist;

import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAllArtistTest {

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    @Test
    void getAllArtistTest() {
        // Сколько художников должно вернуться. Это значение по умолчанию
        final int defaultArtistCount = 10;

        // Отправляем запрос, получаем список первых 10 художников (значение по умолчанию)
        List<ArtistJson> artistJsonList = artistGatewayClient.getAllArtists();

        //Идем в базу, и получаем 10 первых художников
        List<ArtistEntity> artistEntityList = artistRepository.findAllArtists(defaultArtistCount);
        assertEquals(artistJsonList.size(), artistEntityList.size());

        for (ArtistJson actualArtist : artistJsonList) {
            ArtistEntity expectedArtist = artistEntityList.stream()
                                                          .filter(artist -> artist.getId().equals(actualArtist.id()))
                                                          .findFirst()
                                                          .orElseThrow(() -> new AssertionError(
                                                                  "Художник c id " + actualArtist.id() + " отсутствует в базе")
                                                          );

            assertAll("Проверка полей художника",
                    () -> assertEquals(actualArtist.id(), expectedArtist.getId()),
                    () -> assertEquals(actualArtist.name(), expectedArtist.getName()),
                    () -> assertEquals(actualArtist.biography(), expectedArtist.getBiography())
            );
        }
    }
}
