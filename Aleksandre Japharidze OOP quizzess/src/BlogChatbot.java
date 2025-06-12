import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class BlogChatbot {
    private static final String BASE_URL = "https://max.ge/q45/49162738/index.php";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to BlogBot!");
        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1. View all blog posts");
            System.out.println("2. Create a new blog post");
            System.out.println("3. View site statistics");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    getAllBlogs();
                    break;
                case "2":
                    createBlog();
                    break;
                case "3":
                    viewStats();
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void getAllBlogs() {
        try {
            URI uri = new URI(BASE_URL + "?api=blogs");
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("\n--- Blog Posts ---");
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Error fetching blogs: " + e.getMessage());
        }
    }

    private static void createBlog() {
        try {
            System.out.print("Enter blog title: ");
            String title = scanner.nextLine();
            System.out.print("Enter blog content: ");
            String content = scanner.nextLine();
            System.out.print("Enter author name: ");
            String author = scanner.nextLine();

            String json = String.format(
                    "{\"title\":\"%s\", \"content\":\"%s\", \"author\":\"%s\"}",
                    escapeJson(title), escapeJson(content), escapeJson(author)
            );

            URI uri = new URI(BASE_URL + "?api=blogs");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("\n--- Server Response ---");
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Error creating blog: " + e.getMessage());
        }
    }

    private static void viewStats() {
        try {
            URI uri = new URI(BASE_URL + "?api=stats");
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("\n--- Site Statistics ---");
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Error fetching statistics: " + e.getMessage());
        }
    }

    // Simple escape to prevent breaking JSON strings
    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}
