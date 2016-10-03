package msd.com.utils;

public class InteractRecords {

    private String interactedUser;
    private String interactedTree;
    private Object interactedDate;
    private String interactedMedia;

    public String getInteractedMedia() {
        return interactedMedia;
    }

    public void setInteractedMedia(String interactedMedia) {
        this.interactedMedia = interactedMedia;
    }

    public void setInteractedUser(String interactedUser) {
        this.interactedUser = interactedUser;
    }

    public void setInteractedTree(String interactedTree) {
        this.interactedTree = interactedTree;
    }

    public void setInteractedDate(Object interactedDate) {
        this.interactedDate = interactedDate;
    }



    public Object getInteractedDate() {
        return interactedDate;
    }

    public String getInteractedTree() {
        return interactedTree;
    }

    public String getInteractedUser() {
        return interactedUser;
    }




}
