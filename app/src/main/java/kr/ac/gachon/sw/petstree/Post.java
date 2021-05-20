package kr.ac.gachon.sw.petstree;

public class Post {
    private String title;
    private String author;
    private String body;
    private int num_comments;

    public Post(String title, String author, String body, int num_comments) {

        this.title = title;
        this.author = author;
        this.body = body;
        this.num_comments = num_comments;

    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getBody() {
        return body;
    }
    public String getNum_comments() { return Integer.toString(num_comments); }
}
