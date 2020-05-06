package com.example.travelbuddyapp.Model;

import java.util.List;

public class Rating {

private float noOfRating;
private float totalRatings;
private List<String> raters;

public Rating(){

}

public Rating(float noOfRating, float totalRatings, List<String> raters ){
    this.noOfRating = noOfRating;
    this.totalRatings = totalRatings;
    this.raters = raters;
}

public float getNoOfRating(){
    return this.noOfRating;
}

public float getTotalRatings(){
    return this.totalRatings;
}

public void addNoOfRating(){
    noOfRating++;
}

public void addTotalRating(float rating){
    totalRatings = totalRatings + rating;
}

public List<String> getRaters(){
    return this.raters;
}

}
