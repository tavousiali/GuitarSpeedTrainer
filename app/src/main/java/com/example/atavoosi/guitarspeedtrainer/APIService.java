package com.example.atavoosi.guitarspeedtrainer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("dl/enLuw/cAO4GGqNI1/similar_app.json")
    Call<SimilarAppModel> getSimilarApp();
}

class SimilarAppModel {
    public List<SimilarApp> SimilarApp;

    public class SimilarApp {
        public String imageUrl;
        public String title;
        public String desc;
    }
}