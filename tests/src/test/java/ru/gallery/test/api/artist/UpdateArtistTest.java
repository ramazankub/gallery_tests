package ru.gallery.test.api.artist;

import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ArtistJson;
import ru.gallery.service.ArtistGatewayClient;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;
import ru.gallery.utils.JsonUtils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;
import static ru.gallery.utils.DataUtils.randomUsername;

public class UpdateArtistTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private final ArtistGatewayClient artistGatewayClient = new ArtistGatewayClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private String token;

    @BeforeEach
    void createUserAndLogin() {
        String username = randomUsername();
        authApiClient.createUser(username, DEFAULT_PASSWORD);
        token = authApiClient.login(username, DEFAULT_PASSWORD);
    }

    @Test
    void updateArtistTest() {
        ArtistJson addedArtist = ArtistJson.builder()
                                           .name(randomArtistName())
                                           .biography(randomText())
                                           .photo("")
                                           .build();
        // Сначала создаем художника
        Response<ResponseBody> addArtistResponse = artistGatewayClient.addArtist(token, addedArtist);
        assertTrue(addArtistResponse.isSuccessful());
        assertNotNull(addArtistResponse.body());
        ArtistJson addedArtistFromResponse = JsonUtils.readBody(addArtistResponse.body(), ArtistJson.class);

        // Отправляем запрос на получение художника по id
        Response<ResponseBody> getArtistResponse = artistGatewayClient.getArtist(addedArtistFromResponse.id().toString());
        assertTrue(getArtistResponse.isSuccessful());
        assertNotNull(getArtistResponse.body());
        ArtistJson updatedArtistFromResponse = JsonUtils.readBody(getArtistResponse.body(), ArtistJson.class);

        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        // Готовим нового художника с новыми данными
        ArtistJson updatedArtist = ArtistJson.builder()
                                             .id(addedArtistFromResponse.id())
                                             .name(randomArtistName())
                                             .biography(randomText())
                                             .photo(photo)
                                             .build();
        // Обновляем художника
        Response<ResponseBody> response = artistGatewayClient.updateArtist(token, updatedArtist);
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        ArtistJson actualArtistResponse = JsonUtils.readBody(response.body(), ArtistJson.class);

        // Проверяем что в ответе художник уже обновленный
        assertAll("Проверка полей художника, которого возвращает updateArtist",
                () -> assertEquals(updatedArtist.id(), actualArtistResponse.id()),
                () -> assertEquals(updatedArtist.name(), actualArtistResponse.name()),
                () -> assertEquals(updatedArtist.biography(), actualArtistResponse.biography()),
                () -> assertEquals(updatedArtist.photo(), actualArtistResponse.photo())
        );

        // Получаем художника из базы по id
        ArtistEntity actualArtistDb = artistRepository.findArtistById(updatedArtistFromResponse.id());
        // Проверяем что художник в ответе, такой же как и в базе
        assertAll("Проверка полей художника из rococo-artist",
                () -> assertEquals(actualArtistResponse.id(), actualArtistDb.getId()),
                () -> assertEquals(updatedArtist.name(), actualArtistDb.getName()),
                () -> assertEquals(updatedArtist.biography(), actualArtistDb.getBiography())
        );
    }
}
