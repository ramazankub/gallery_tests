package ru.gallery.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.gallery.model.ArtistJson;
import ru.gallery.model.PageResponse;

// В этом классе описываются методы, которые вызываем в автотестах
public interface ArtistGatewayApi {

    // Тип запроса и путь
    @GET("/api/artist/{id}")
    // ArtistJson - это body, который мы будем ждать в ответе
    Call<ArtistJson> getArtist(@Path("id") String id);

    @GET("/api/artist")
    Call<PageResponse<ArtistJson>> getAllArtists();

    @POST("/api/artist")
    Call<ArtistJson> addArtist(@Header("Authorization") String bearerToken,
                               @Body ArtistJson artist);

    @PATCH("/api/artist")
    Call<ArtistJson> updateArtist(@Header("Authorization") String bearerToken,
                                  @Body ArtistJson artist);
}
