package com.moc.wellness.exception.notFound;


import com.moc.wellness.exception.common.IdNameException;
import lombok.Getter;

@Getter
public class NotFoundEntity extends IdNameException {


    public NotFoundEntity(String name, Long id) {
        super(name, id, "Entity " + name + " with id " + id.toString() + " was not found!");

    }
}
