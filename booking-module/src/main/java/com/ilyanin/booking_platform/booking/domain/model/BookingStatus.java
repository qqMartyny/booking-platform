package com.ilyanin.booking_platform.booking.domain.model;

public enum BookingStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(BookingStatus next) {
            return next == APPROVED 
                || next == REJECTED
                || next == CANCELLED;
        }
    },
    APPROVED {
        @Override
        public boolean canTransitionTo(BookingStatus next) {
            return next == CHANGE_REQUESTED
                || next == COMPLETED
                || next == CANCELLED;
        }
    },
    REJECTED {
        @Override
        public boolean canTransitionTo(BookingStatus next) {
            return false;    
        }
    },
    CANCELLED {
        @Override
        public boolean canTransitionTo(BookingStatus next) {
            return false;
        }
    },
    CHANGE_REQUESTED {
        @Override
        public boolean canTransitionTo(BookingStatus next) {
            return next == APPROVED
                || next == CANCELLED;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitionTo(BookingStatus next) {
            return false;    
        }
    };

    public abstract boolean canTransitionTo(BookingStatus next);
}
