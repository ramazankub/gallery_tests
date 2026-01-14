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
        // Сколько художников должно вернуться. Это значение по умолчанию
        final int defaultArtistCount = 10;

        // Отправляем запрос, получаем список первых 10 художников (значение по умолчанию)
        List<ArtistJson> artistJsonList = artistGatewayClient.getAllArtists();

        //Идем в базу, и получаем 10 первых художников
        List<ArtistEntity> artistEntityList = artistRepository.findAllArtists(defaultArtistCount);
        // В идеале проверять все поля всех художников, но здесь проверяем только размер
        assertEquals(artistJsonList.size(), artistEntityList.size());
    }
}
