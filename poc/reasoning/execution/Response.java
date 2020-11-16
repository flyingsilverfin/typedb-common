package grakn.common.poc.reasoning.execution;

import grakn.common.poc.reasoning.Request;

public interface Response {
    Request sourceRequest();

    boolean isAnswer();

    boolean isExhausted();

    default Response.Answer asAnswer() {
        throw new ClassCastException("Cannot cast " + this.getClass().getSimpleName() + " to " + Response.Answer.class.getSimpleName());
    }

    default Response.Exhausted asExhausted() {
        throw new ClassCastException("Cannot cast " + this.getClass().getSimpleName() + " to " + Response.Exhausted.class.getSimpleName());
    }

    interface Answer<T extends Answer<T>> extends Response {
        T getThis();
    }

    interface Exhausted extends Response { }
}