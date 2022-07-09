package com.sunrisestudio.util.event;

import java.lang.annotation.Annotation;

public interface AnnotationPredicateCallBack<A> {
    String getType(A a);

    boolean instanceOf(Annotation a);
}
