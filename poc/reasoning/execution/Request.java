package grakn.common.poc.reasoning.execution;

import grakn.common.concurrent.actor.Actor;

import javax.annotation.Nullable;

public interface Request {
    @Nullable
    Actor<? extends ExecutionActor<?>> sender();

    Actor<? extends ExecutionActor<?>> receiver();
}
