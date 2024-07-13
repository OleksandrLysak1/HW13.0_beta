package org.example.Exercise1;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Exercise1 {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users";

    private final HttpClient httpClient;

    public Exercise1() {
        this.httpClient = HttpClients.createDefault();
    }

    // Метод для створення нового користувача
    public String createNewUser(String jsonBody) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        request.setEntity(new StringEntity(jsonBody));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(request);
        return handleResponse(response);
    }

    // Метод для оновлення користувача
    public String updateUser(int userId, String jsonBody) throws IOException {
        HttpPut request = new HttpPut(BASE_URL + "/" + userId);
        request.setEntity(new StringEntity(jsonBody));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(request);
        return handleResponse(response);
    }

    // Метод для видалення користувача
    public boolean deleteUser(int userId) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/" + userId);

        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode >= 200 && statusCode < 300; // Перевірка на успішний статус код (2xx)
    }

    // Метод для отримання інформації про всіх користувачів
    public String getAllUsers() throws IOException {
        HttpGet request = new HttpGet(BASE_URL);

        HttpResponse response = httpClient.execute(request);
        return handleResponse(response);
    }

    // Метод для отримання інформації про користувача за id
    public String getUserById(int userId) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/" + userId);

        HttpResponse response = httpClient.execute(request);
        return handleResponse(response);
    }

    // Метод для отримання інформації про користувача за username
    public String getUserByUsername(String username) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "?username=" + username);

        HttpResponse response = httpClient.execute(request);
        return handleResponse(response);
    }

    // Внутрішній метод для обробки відповіді
    private String handleResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        EntityUtils.consume(entity); // Забезпечуємо вивільнення ресурсів
        return responseString;
    }

    public static void main(String[] args) {
        Exercise1 apiClient = new Exercise1();

        try {
            // Створення нового користувача
            String newUserJson = "{ \"name\": \"John Doe\", \"username\": \"johndoe\", \"email\": \"johndoe@example.com\" }";
            String createUserResponse = apiClient.createNewUser(newUserJson);
            System.out.println("Create User Response: " + createUserResponse);

            // Оновлення користувача
            int userIdToUpdate = 1;
            String updateUserJson = "{ \"name\": \"John Smith\", \"username\": \"johnsmith\", \"email\": \"johnsmith@example.com\" }";
            String updateUserResponse = apiClient.updateUser(userIdToUpdate, updateUserJson);
            System.out.println("Update User Response: " + updateUserResponse);

            // Видалення користувача
            int userIdToDelete = 1;
            boolean deleteUserResponse = apiClient.deleteUser(userIdToDelete);
            System.out.println("Delete User Response: " + deleteUserResponse);

            // Отримання всіх користувачів
            String allUsersResponse = apiClient.getAllUsers();
            System.out.println("All Users Response: " + allUsersResponse);

            // Отримання користувача за ID
            int userIdToGet = 1;
            String getUserByIdResponse = apiClient.getUserById(userIdToGet);
            System.out.println("Get User By ID Response: " + getUserByIdResponse);

            // Отримання користувача за username
            String usernameToGet = "Bret";
            String getUserByUsernameResponse = apiClient.getUserByUsername(usernameToGet);
            System.out.println("Get User By Username Response: " + getUserByUsernameResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
