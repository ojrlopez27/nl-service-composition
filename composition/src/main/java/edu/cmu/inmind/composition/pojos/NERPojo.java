package edu.cmu.inmind.composition.pojos;

import java.util.Date;

/**
 * Created by oscarr on 8/14/18.
 */
public class NERPojo {
    private String word;
    private String annotation;
    private String normalizedAnnotation;
    private int begin;
    private int end;
    private Date date;
    private NERPojo previous;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getNormalizedAnnotation() {
        return normalizedAnnotation;
    }

    public void setNormalizedAnnotation(String normalizedAnnotation) {
        this.normalizedAnnotation = normalizedAnnotation;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public NERPojo getPrevious() {
        return previous;
    }

    public void setPrevious(NERPojo previous) {
        this.previous = previous;
    }
}
