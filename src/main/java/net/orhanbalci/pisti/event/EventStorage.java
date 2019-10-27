package net.orhanbalci.pisti.event;

public class EventStorage<T> {
  private T event;

  public EventStorage(T newEvent) {
    event = newEvent;
  }

  public T getEvent() {
    return event;
  }
}
