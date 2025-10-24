package ru.gallery.service;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.gallery.api.ArtistGatewayApi;
import ru.gallery.config.Config;
import ru.gallery.model.ArtistJson;
import ru.gallery.model.PageResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArtistGatewayRestClient {

    private static final Config CFG = Config.getInstance();

    private final ArtistGatewayApi artistGatewayApi;

    public ArtistGatewayRestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CFG.gatewayUrl())
                .addConverterFactory(JacksonConverterFactory.create())
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
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    public List<ArtistJson> getAllArtists() {
        final Response<PageResponse<ArtistJson>> response;
        try {
            response = artistGatewayApi.getAllArtists().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();

        return response.body().content();
    }

    public ArtistJson addArtist(String token, ArtistJson artist) {
        final Response<ArtistJson> response;
        try {
            response = artistGatewayApi.addArtist(token, artist).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    public ArtistJson updateArtist(String token, ArtistJson artist) {
        final Response<ArtistJson> response;
        try {
            response = artistGatewayApi.updateArtist(token, artist).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }
}
