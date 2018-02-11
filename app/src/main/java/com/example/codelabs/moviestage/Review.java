package com.example.codelabs.moviestage;

/**
 * Created by varshaa on 21-01-2018.
 */

public  class Review
{   String reviewId;
    String author;
    String content;

    public Review(String reviewId, String author, String content) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }


}
