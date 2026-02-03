package ru.gallery.test.api.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayClient;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;
import static ru.gallery.utils.DataUtils.randomUsername;

public class AddArtistTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private String token;

    // @BeforeEach запускает блок кода перед каждым тестам. Нужно, чтобы в каждом тесте был новый пользователь
    @BeforeEach
    void createUserAndLogin() {
        String username = randomUsername();
        authApiClient.createUser(username, DEFAULT_PASSWORD);
        token = authApiClient.login(username, DEFAULT_PASSWORD);
    }

    @Test
    void addArtistTest() {
        // Создаем художника, чтобы положить его в базу
        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");

        ArtistJson expectedArtist = ArtistJson.builder()
                                              .name(randomArtistName())
                                              .biography(randomText())
                                              .photo(photo)
                                              .build();

        // Отправляем запрос на добавление художника, который положит его в базу. И нам возвращается сущность того же художника
        ArtistJson actualArtistResponse = artistGatewayClient.addArtist(token, expectedArtist);

        // Проверяем что вернулось то же самое, что мы и отправляли
        // assertAll реализует софт ассерты. Чтобы тест не упал при первом несовпадении, а провел все проверки, и
        // сообщил результат в конце
        assertAll("Проверка полей художника, которого возвращает addArtist",
                () -> assertEquals(expectedArtist.name(), actualArtistResponse.name()),
                () -> assertEquals(expectedArtist.biography(), actualArtistResponse.biography()),
                () -> assertEquals(expectedArtist.photo(), actualArtistResponse.photo())
        );

        // Идем в базу напрямую и достаем добавленного художника
        ArtistEntity actualArtistDb = artistRepository.findArtistById(actualArtistResponse.id());

        // Проверяем что в базе лежит тот же художник что и вернулся в методе
        // На проверку фото здесь подзабил, но проверять его, конечно, нужно :)
        assertAll("Проверка полей художника из rococo-artist",
                () -> assertEquals(actualArtistResponse.id(), actualArtistDb.getId()),
                () -> assertEquals(expectedArtist.name(), actualArtistDb.getName()),
                () -> assertEquals(expectedArtist.biography(), actualArtistDb.getBiography())
        );
    }
}
