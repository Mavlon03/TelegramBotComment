package uz.pdp.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public interface DB {
    List<TgUser> TG_USERS = new ArrayList<>();
    List<User> USERS = new ArrayList<>();
    List<Post> POSTS = new ArrayList<>();
    List<TgComment> COMMENTS = new ArrayList<>();

    static void generate(){

        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            Type userLIstType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(response.body(), userLIstType);
            USERS.addAll(users);

            HttpClient httpClientPost = HttpClient.newHttpClient();
            HttpRequest httpRequestPost = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                    .GET()
                    .build();
            HttpResponse<String> responsePost = httpClientPost.send(httpRequestPost, HttpResponse.BodyHandlers.ofString());

            Gson gsonPost = new Gson();
            Type postList = new TypeToken<List<Post>>() {}.getType();
            List<Post> tgPosts = gsonPost.fromJson(responsePost.body(), postList);
            POSTS.addAll(tgPosts);

            HttpClient httpClientComment = HttpClient.newHttpClient();
            HttpRequest httpRequestComment = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/comments"))
                    .GET()
                    .build();
            HttpResponse<String> responseComment = httpClientComment.send(httpRequestComment, HttpResponse.BodyHandlers.ofString());

            Gson gsonComment = new Gson();
            Type commentList = new TypeToken<List<TgComment>>() {}.getType();
            List<TgComment> tgComments = gsonComment.fromJson(responseComment.body(), commentList);
            COMMENTS.addAll(tgComments);


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
