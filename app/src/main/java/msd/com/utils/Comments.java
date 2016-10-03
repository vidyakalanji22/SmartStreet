package msd.com.utils;

/**
 * Pojo class for users to map with firebase
 */
public class Comments{

    private String commentText;
    private Object created;
    private double rating;
    private String user;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;

    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
