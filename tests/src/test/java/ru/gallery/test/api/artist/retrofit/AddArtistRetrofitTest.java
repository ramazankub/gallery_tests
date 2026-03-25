package ru.gallery.test.api.artist.retrofit;

import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.gallery.data.ArtistRepository;
import ru.gallery.data.entity.ArtistEntity;
import ru.gallery.model.ApiVersion;
import ru.gallery.model.ArtistJson;
import ru.gallery.model.ErrorBodyJson;
import ru.gallery.service.ArtistGatewayRetrofitClient;
import ru.gallery.service.AuthApiClient;
import ru.gallery.utils.DataUtils;
import ru.gallery.utils.JsonUtils;
import ru.gallery.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.gallery.utils.DataUtils.DEFAULT_PASSWORD;
import static ru.gallery.utils.DataUtils.randomArtistName;
import static ru.gallery.utils.DataUtils.randomText;
import static ru.gallery.utils.DataUtils.randomUsername;

public class AddArtistRetrofitTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private final ArtistGatewayRetrofitClient artistGatewayRetrofitClient = new ArtistGatewayRetrofitClient();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private String token;

    // @BeforeEach запускает блок кода перед каждым тестам. Нужно, чтобы в каждом тесте был новый пользователь
    @BeforeEach
    void createUserAndLogin() {
        String username = randomUsername();
        authApiClient.createUser(username, DEFAULT_PASSWORD);
        token = authApiClient.login(username, DEFAULT_PASSWORD);
    }

    // !!!!!!!
    // Это пример теста с использованием библиотеки Retrofit вместо Rest Assured. Внутри теста разница в строках 60-66
    // !!!!!!!
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
        Response<ResponseBody> response = artistGatewayRetrofitClient.addArtist(token, expectedArtist);
        // Проверяем, что запрос успешный
        assertTrue(response.isSuccessful());
        // Проверяем что тело ответа не null
        assertNotNull(response.body());
        // Преобразуем response.body() в ArtistJson
        ArtistJson actualArtistResponse = JsonUtils.readBody(response.body(), ArtistJson.class);

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
                () -> assertEquals(expectedArtist.biography(), actualArtistDb.getBiography()),
                () -> assertEquals(expectedArtist.photo(), StringUtils.fromUtf8(actualArtistDb.getPhoto()))
        );
    }

    @Test
    void addArtistTestWithoutName() {
        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson expectedArtist = ArtistJson.builder()
                                              .biography(randomText()) // Биографию и фото тоже задаем, так как они обязательны
                                              .photo(photo)
                                              .build();

        Response<ResponseBody> response = artistGatewayRetrofitClient.addArtist(token, expectedArtist);
        assertFalse(response.isSuccessful());
        assertNotNull(response.errorBody());
        ErrorBodyJson errorBodyJson = JsonUtils.readBody(response.errorBody(), ErrorBodyJson.class);

        assertAll("Проверка ответа c ошибкой",
                () -> assertEquals(ApiVersion.V1.toString(), errorBodyJson.apiVersion()),
                () -> assertEquals("400", errorBodyJson.error().code()),
                () -> assertEquals("Name cannot be blank", errorBodyJson.error().message())
        );
    }

    @Test
    void addArtistTestWithoutBiography() {
        String photo = DataUtils.getImageByPathOrEmpty("img/artists/botticelli.jpg");
        ArtistJson expectedArtist = ArtistJson.builder()
                                              .name(randomArtistName())
                                              .photo(photo)
                                              .build();

        Response<ResponseBody> response = artistGatewayRetrofitClient.addArtist(token, expectedArtist);
        assertFalse(response.isSuccessful());
        assertNotNull(response.errorBody());
        ErrorBodyJson errorBodyJson = JsonUtils.readBody(response.errorBody(), ErrorBodyJson.class);

        assertAll("Проверка ответа c ошибкой",
                () -> assertEquals(ApiVersion.V1.toString(), errorBodyJson.apiVersion()),
                () -> assertEquals("400", errorBodyJson.error().code()),
                () -> assertEquals("Biography cannot be blank", errorBodyJson.error().message())
        );
    }
}
