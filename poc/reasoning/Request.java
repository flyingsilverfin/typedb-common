package grakn.common.poc.reasoning;


import grakn.common.concurrent.actor.Actor;
import grakn.common.poc.reasoning.execution.ExecutionActor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static grakn.common.collection.Collections.list;

public class Request implements grakn.common.poc.reasoning.execution.Request {
    private final Path path;
    private final List<Long> partialAnswer;
    private final List<Object> constraints;
    private final List<Object> unifiers;

    public Request(Path path,
                   List<Long> partialAnswer,
                   List<Object> constraints,
                   List<Object> unifiers) {
        this.path = path;
        this.partialAnswer = partialAnswer;
        this.constraints = constraints;
        this.unifiers = unifiers;
    }

    public Path path() {
        return path;
    }

    @Override
    @Nullable
    public Actor<? extends ExecutionActor<?>> sender() {
        if (path.path.size() < 2) {
            return null;
        }
        return path.path.get(path.path.size() - 2);
    }

    @Override
    public Actor<? extends ExecutionActor<?>> receiver() {
        return path.path.get(path.path.size() - 1);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(path, request.path) &&
                Objects.equals(partialAnswer, request.partialAnswer()) &&
                Objects.equals(constraints, request.constraints()) &&
                Objects.equals(unifiers, request.unifiers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, partialAnswer, constraints, unifiers);
    }

    @Override
    public String toString() {
        return "Req(send=" + (sender() == null ? "<none>" : sender().state.name) + ", pAns=" + partialAnswer + ")";
    }

    public static class Path {
        final List<Actor<? extends ExecutionActor<?>>> path;

        public Path(Actor<? extends ExecutionActor<?>> sender) {
            this(list(sender));
        }

        private Path(List<Actor<? extends ExecutionActor<?>>> path) {
            assert !path.isEmpty() : "Path cannot be empty";
            this.path = path;
        }

        public Path append(Actor<? extends ExecutionActor<?>> actor) {
            List<Actor<? extends ExecutionActor<?>>> appended = new ArrayList<>(path);
            appended.add(actor);
            return new Path(appended);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path1 = (Path) o;
            return Objects.equals(path, path1.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }
}