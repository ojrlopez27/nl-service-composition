package edu.cmu.inmind.services.pojos;

import edu.cmu.inmind.services.commons.GenericPOJO;
import java.util.Date;

/**
 * Created by oscarr on 8/14/18.
 */
public class NERPOJO extends GenericPOJO {
    private String word;
    private String annotation;
    private String normalizedAnnotation;
    private int begin;
    private int end;
    private Date date;
    private NERPOJO previous;

    protected NERPOJO(NERPOJOBuilder nerPojoBuilder) {
        super(nerPojoBuilder);
        this.word = nerPojoBuilder.word;
        this.annotation = nerPojoBuilder.annotation;
        this.normalizedAnnotation = nerPojoBuilder.normalizedAnnotation;
        this.begin = nerPojoBuilder.begin;
        this.end = nerPojoBuilder.end;
        this.date = nerPojoBuilder.date;
        this.previous = nerPojoBuilder.previous;
    }

    public String getWord() {
        return word;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getNormalizedAnnotation() {
        return normalizedAnnotation;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public Date getDate() {
        return date;
    }

    public NERPOJO getPrevious() {
        return previous;
    }

    public class NERPOJOBuilder extends PojoBuilder {
        private String word;
        private String annotation;
        private String normalizedAnnotation;
        private int begin;
        private int end;
        private Date date;
        private NERPOJO previous;

        public NERPOJOBuilder(String serviceId, String alias, String methodName) {
            super(serviceId, alias, methodName);
        }

        public NERPOJOBuilder setWord(String word) {
            this.word = word;
            return this;
        }

        public NERPOJOBuilder setAnnotation(String annotation) {
            this.annotation = annotation;
            return this;
        }

        public NERPOJOBuilder setNormalizedAnnotation(String normalizedAnnotation) {
            this.normalizedAnnotation = normalizedAnnotation;
            return this;
        }

        public NERPOJOBuilder setBegin(int begin) {
            this.begin = begin;
            return this;
        }

        public NERPOJOBuilder setEnd(int end) {
            this.end = end;
            return this;
        }

        public NERPOJOBuilder setDate(Date date) {
            this.date = date;
            return this;
        }

        public NERPOJOBuilder setPrevious(NERPOJO previous) {
            this.previous = previous;
            return this;
        }

        public NERPOJO build() {
            return new NERPOJO(this);
        }
    }
}
