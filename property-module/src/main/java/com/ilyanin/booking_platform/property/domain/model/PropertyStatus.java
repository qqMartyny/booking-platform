package com.ilyanin.booking_platform.property.domain.model;

public enum PropertyStatus {
    DRAFT {
        @Override
        public boolean canTransitionTo(PropertyStatus next) {
            return next == PUBLISHED;
        }
    },
    PUBLISHED {
        @Override
        public boolean canTransitionTo(PropertyStatus next) {
            return next == ARCHIVED
                || next == DRAFT;
        }
    },
    ARCHIVED {
        @Override
        public boolean canTransitionTo(PropertyStatus next) {
            return next == PUBLISHED;
        }
    };
    
    public abstract boolean canTransitionTo(PropertyStatus next);
}
