package com.moc.wellness.exception.action;

import com.moc.wellness.exception.common.IdNameException;
import lombok.Getter;

@Getter
public class SubEntityUsed extends IdNameException {


    public SubEntityUsed(String name, Long id) {
        super(name, id, "Entity " + name + " with id " + id.toString() + " is used!");

    }
    
}
