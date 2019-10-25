package net.orhanbalci.pisti.event;

import io.vavr.control.Either;

public interface Visitable<V, G> {
    public Either<V, G> allowVisit(Visitor<V,G> v);
}