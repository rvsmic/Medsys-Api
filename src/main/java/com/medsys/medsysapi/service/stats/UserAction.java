package com.medsys.medsysapi.service.stats;

import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public final class UserAction {
    public final int id;
    public final Date date;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserAction other) {
            return this.id == other.id;
        }
        return false;
    }
}
