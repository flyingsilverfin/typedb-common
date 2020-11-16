package grakn.common.poc.reasoning;

import java.util.List;

public class Response {

    static class Answer implements grakn.common.poc.reasoning.execution.Response.Answer<Answer> {
        private final Request sourceRequest;
        private final List<Long> partialAnswer;
        private final List<Object> constraints;
        private final List<Object> unifiers;

        public Answer(final Request sourceRequest,
                      final List<Long> partialAnswer,
                      final List<Object> constraints,
                      final List<Object> unifiers) {
            this.sourceRequest = sourceRequest;
            this.partialAnswer = partialAnswer;
            this.constraints = constraints;
            this.unifiers = unifiers;
        }

        @Override
        public Response.Answer getThis() {
            return this;
        }

        @Override
        public Request sourceRequest() {
            return sourceRequest;
        }

        public List<Long> partialAnswer() {
            return partialAnswer;
        }

        public List<Object> constraints() {
            return constraints;
        }

        public List<Object> unifiers() {
            return unifiers;
        }

        @Override
        public boolean isAnswer() { return true; }

        @Override
        public boolean isExhausted() { return false; }

        @Override
        public Answer asAnswer() {
            return this;
        }
    }

    static class Exhausted implements grakn.common.poc.reasoning.execution.Response.Exhausted {
        private final Request sourceRequest;

        public Exhausted(final Request sourceRequest) {
            this.sourceRequest = sourceRequest;
        }

        @Override
        public Request sourceRequest() {
            return sourceRequest;
        }

        @Override
        public boolean isAnswer() { return false; }

        @Override
        public boolean isExhausted() { return true; }

        @Override
        public Exhausted asExhausted() {
            return this;
        }
    }
}
