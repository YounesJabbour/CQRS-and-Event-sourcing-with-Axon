package com.axon_springboot.axon_springboot.commonapi.events;


import lombok.Getter;

public abstract class BaseEvent<T>  {
   @Getter
   public final T id;

    public BaseEvent(T id) {
        this.id = id;
    }
}
