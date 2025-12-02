package ru.gallery.service;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.gallery.api.ArtistGatewayApi;
import ru.gallery.config.Config;
import ru.gallery.model.ArtistJson;
import ru.gallery.model.PageResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.gallery.utils.ClientUtils.getOkHttpClient;

public class ArtistGatewayClient {

    private static final Config CFG = Config.getInstance();

    private final ArtistGatewayApi artistGatewayApi;

    public ArtistGatewayClient() {
        OkHttpClient client = getOkHttpClient(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CFG.gatewayUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
        this.artistGatewayApi = retrofit.create(ArtistGatewayApi.class);
    }

    public ArtistJson getArtist(String id) {
        final Response<ArtistJson> response;
        try {
            response = artistGatewayApi.getArtist(id).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        return response.body();
    }

    public List<ArtistJson> getAllArtists() {
        final Response<PageResponse<ArtistJson>> response;
        try {
            response = artistGatewayApi.getAllArtists().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertNotNull(response.body());

        return response.body().content();
    }

    public ArtistJson addArtist(String token, ArtistJson artist) {
        final Response<ArtistJson> response;
        try {
            response = artistGatewayApi.addArtist(token, artist).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        return response.body();
    }

    public ArtistJson updateArtist(String token, ArtistJson artist) {
        final Response<ArtistJson> response;
        try {
            response = artistGatewayApi.updateArtist(token, artist).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        return response.body();
    }
}
