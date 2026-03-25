CREATE TABLE bookings(
    id UUID NOT NULL,
    guest_id UUID NOT NULL,
    room_id UUID NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_bookings_room_status_dates
    ON bookings (room_id, status, start_date, end_date);
