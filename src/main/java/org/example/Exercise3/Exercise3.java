package org.example.Exercise3;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Exercise3 {
    public static void main(String[] args) {
        try {
            int userId = 1; // ID користувача
            List<Post> posts = getPosts(userId);
            Post latestPost = posts.stream().max(Comparator.comparingInt(Post::getId)).orElse(null);

            if (latestPost != null) {
                List<Comment> comments = getComments(latestPost.getId());
                writeCommentsToFile(userId, latestPost.getId(), comments);
                System.out.println("Comments written to file successfully.");
            } else {
                System.out.println("No posts found for user " + userId);
            }

            // Додатково викликаємо новий метод для виводу відкритих задач
            printOpenTasks(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Post> getPosts(int userId) throws IOException {
        String urlString = "https://jsonplaceholder.typicode.com/users/" + userId + "/posts";
        String response = getHttpResponse(urlString);
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(response, Post[].class));
    }

    private static List<Comment> getComments(int postId) throws IOException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";
        String response = getHttpResponse(urlString);
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(response, Comment[].class));
    }

    private static void writeCommentsToFile(int userId, int postId, List<Comment> comments) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
        mapper.writeValue(new File(fileName), comments);
    }

    private static String getHttpResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return response.toString();
    }

    // Новий метод для виводу відкритих задач
    private static void printOpenTasks(int userId) throws IOException {
        String urlString = "https://jsonplaceholder.typicode.com/users/" + userId + "/todos";
        String response = getHttpResponse(urlString);
        ObjectMapper mapper = new ObjectMapper();
        Todo[] todos = mapper.readValue(response, Todo[].class);

        System.out.println("Open tasks for user " + userId + ":");
        for (Todo todo : todos) {
            if (!todo.isCompleted()) {
                System.out.println("Task #" + todo.getId() + ": " + todo.getTitle());
            }
        }
    }
}

class Post {
    private int userId;
    private int id;
    private String title;
    private String body;

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}

class Comment {
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;

    // Getters and setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}

class Todo {
    private int userId;
    private int id;
    private String title;
    private boolean completed;

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
