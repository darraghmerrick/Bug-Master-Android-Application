package com.google.developer.bugmaster.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Pegasus on 13/10/2017.
 * Utility for the Quiz activity to randomize questions
 * and remove used questions from the list
 */

public class QuizUtils {
    //Static method requires explicit <T>
    //declaring an arbitrary type T for this method,
    //can be anything (non-primitive) for each call of the method.
    public static <T> ArrayList<T> shuffleArrayList(ArrayList<T> quizList) {
        long seed = System.nanoTime();
        Collections.shuffle(quizList, new Random(seed));
        return quizList;
    }

    public static <T> ArrayList<T> shrinkTo(ArrayList<T> list, int newSize) {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.addAll(list.subList(0, newSize));
        return arrayList;
    }
}
